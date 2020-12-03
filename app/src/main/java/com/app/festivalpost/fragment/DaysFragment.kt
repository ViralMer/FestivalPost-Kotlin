package com.app.festivalpost.fragment

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.GridSpacingItemDecoration
import com.app.festivalpost.R
import com.app.festivalpost.adapter.DayAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.fragment.DaysFragment
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.FestivalItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*

class DaysFragment : Fragment(), ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var dataArrayList: ArrayList<FestivalItem>? = null
    var btnchoosedate: Button? = null
    var rvdata: RecyclerView? = null
    var picker: DatePickerDialog? = null
    var dateVal = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiManager = ApiManager(activity!!, this@DaysFragment)
        rvdata = view.findViewById<View>(R.id.rvdata) as RecyclerView
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(activity, 3)
        rvdata!!.layoutManager = mLayoutManager
        rvdata!!.addItemDecoration(GridSpacingItemDecoration(3, dpToPx(8), true))
        rvdata!!.itemAnimator = DefaultItemAnimator()
        btnchoosedate = view.findViewById<View>(R.id.btnchoosedate) as Button
        btnchoosedate!!.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            // date picker dialog
            picker = DatePickerDialog(
                activity!!,
                { view, year, monthOfYear, dayOfMonth ->
                    btnchoosedate!!.text =
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    var strDay = dayOfMonth.toString()
                    var strMonth = ""
                    if (dayOfMonth < 12) {
                        strDay = "0$dayOfMonth"
                    }
                    if (monthOfYear + 1 < 12) {
                        strMonth = (monthOfYear + 1).toString()
                    }
                    dateVal = "$year-$strMonth-$strDay"
                    getDays(dateVal)
                }, year, month, day
            )
            picker!!.show()
        }


//        final Calendar cldr = Calendar.getInstance();
//        int day = cldr.get(Calendar.DAY_OF_MONTH);
//        int month = cldr.get(Calendar.MONTH) + 1;
//        int year = cldr.get(Calendar.YEAR);
//
//        String strDay = String.valueOf(day);
//        String strMonth = "";
//
//        if (day < 10) {
//            strDay = "0" + day;
//        }
//
//        if (month < 10) {
//            strMonth = "0" + month;
//        }
//        dateVal = year + "-" + strMonth + "-" + strDay;
        getDays(dateVal)
    }

    fun getDays(dateVal1: String?) {
        Global.showProgressDialog(activity)
        apiManager!!.getdays(
            ApiEndpoints.getdays,
            dateVal1,
            Global.getPreference(Constant.PREF_TOKEN, "")
        )
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(activity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(activity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(activity)
            if (requestService.equals(ApiEndpoints.getdays, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        filldata()
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun filldata() {
        if (dataArrayList!!.size > 0) {
            rvdata!!.visibility = View.VISIBLE
            val adapter = DayAdapter(activity!!, dataArrayList!!)
            rvdata!!.adapter = adapter
        } else {
            rvdata!!.visibility = View.GONE
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(activity)
            Global.showFailDialog(activity, responseString)
        }
    }

    fun processResponse(responseString: String?) {
        dataArrayList = ArrayList()
        status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }
            if (jsonObject.has("ispreminum")) {
                val premium = jsonObject.getBoolean("ispreminum")
                Global.storePreference(Constant.PREF_PREMIUM, premium)
            }
            if (jsonObject.has("current_business")) {
                val businessJsonObject = jsonObject.getJSONObject("current_business")
                Global.storeCurrentBusiness(businessJsonObject.toString())
            } else {
                Global.storeCurrentBusiness("")
            }
            if (jsonObject.has("current_business_new")) {
                val businessJsonObject = jsonObject.getJSONObject("current_business_new")
                Global.storeCurrentBusinessNew(businessJsonObject.toString())
                if (businessJsonObject.getString("busi_logo") != "") {
                    Glide.with(this).asBitmap().load(businessJsonObject.getString("busi_logo"))
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val root = Environment.getExternalStorageDirectory().absolutePath
                                val myDir = File("$root/Imagename")
                                myDir.mkdirs()
                                myDir.mkdir()
                                val fname = "logo.png"
                                val file = File(myDir, fname)
                                if (file.exists()) file.delete()
                                try {
                                    val out = FileOutputStream(file)
                                    resource.compress(Bitmap.CompressFormat.PNG, 100, out)
                                    out.flush()
                                    out.close()
                                } catch (e: Exception) {
                                }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            } else {
                Global.storeCurrentBusinessNew("")
            }
            if (jsonObject.has("festival")) {
                val jsonArray = jsonObject.getJSONArray("festival")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), FestivalItem::class.java)
                        dataArrayList!!.add(f)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(): DaysFragment {
            return DaysFragment()
        }
    }
}