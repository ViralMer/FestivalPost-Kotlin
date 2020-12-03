package com.app.festivalpost.fragment

import android.Manifest
import android.content.Intent
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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.app.festivalpost.ChoosePhotoActivity
import com.app.festivalpost.LoginActivity
import com.app.festivalpost.R
import com.app.festivalpost.VideoDetailActivity
import com.app.festivalpost.activity.AddFirstBusinessActivity
import com.app.festivalpost.adapter.CustomFestivalAdapter
import com.app.festivalpost.adapter.ThisMonthFestivalAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.fragment.HomeFragment
import com.app.festivalpost.fragment.HomeFragment.PagerAdapterNew.PageViewHolder
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.FestivalItem
import com.app.festivalpost.models.FrameListItem
import com.app.festivalpost.models.IncidentsItem
import com.app.festivalpost.utility.RecyclerCoverFlow
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var logout_Status = false
    var message = ""
    var todayFestivalItemArrayList: ArrayList<FestivalItem>? = null
    var thisMonthFestivalItemArrayList: ArrayList<FestivalItem>? = null
    var incidentItemArrayList: ArrayList<FestivalItem>? = null
    var incidentItemArrayList1 = ArrayList<IncidentsItem>()
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var viewPager: RecyclerCoverFlow? = null
    var rvdata: RecyclerView? = null
    var linearLayout: RecyclerView? = null
    var viewall: TextView? = null
    var layincidents: LinearLayout? = null
    var tvprev: TextView? = null
    var tvincident: TextView? = null
    var tvnext: TextView? = null
    var btnVideo: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiManager = ApiManager(activity!!, this@HomeFragment)
        linearLayout = view.findViewById(R.id.customFestival)
        layincidents = view.findViewById(R.id.layincidents)
        tvprev = view.findViewById(R.id.tvprev)
        tvincident = view.findViewById(R.id.tvincident)
        tvnext = view.findViewById(R.id.tvnext)
        viewall = view.findViewById(R.id.viewall)
        btnVideo = view.findViewById(R.id.btnVideo)
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)
        tvprev!!.setOnClickListener(View.OnClickListener {
            current_incident_index = current_incident_index - 1
            if (current_incident_index >= 0) {
                val ff = incidentItemArrayList!![current_incident_index]
                tvincident!!.setText(ff.festName)
                tvnext!!.setVisibility(View.VISIBLE)
            }
            if (current_incident_index == 0) {
                tvprev!!.setVisibility(View.INVISIBLE)
            }
        })
        mSwipeRefreshLayout!!.setOnRefreshListener(OnRefreshListener {
            loadDetails()
            mSwipeRefreshLayout!!.setRefreshing(false)
        })
        openAddImageDialog()

        /*try{
            NetworkStateChecker networkStateChecker=new NetworkStateChecker();
            getActivity().registerReceiver(networkStateChecker,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        catch (Exception e)
        {

        }*/tvnext!!.setOnClickListener(View.OnClickListener {
            current_incident_index = current_incident_index + 1
            if (current_incident_index <= incidentItemArrayList!!.size - 1) {
                val ff = incidentItemArrayList!![current_incident_index]
                tvincident!!.setText(ff.festName)
                tvprev!!.setVisibility(View.VISIBLE)
            }
            if (current_incident_index == incidentItemArrayList!!.size - 1) {
                current_incident_index = incidentItemArrayList!!.size - 1
                tvnext!!.setVisibility(View.INVISIBLE)
            }
        })
        viewPager = view.findViewById(R.id.viewPager)
        viewPager!!.setHasFixedSize(true)
        viewPager!!.setLoop()
        rvdata = view.findViewById<View>(R.id.rvdata) as RecyclerView
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvdata!!.layoutManager = mLayoutManager
        loadDetails()
        btnVideo!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, VideoDetailActivity::class.java)
            startActivity(intent)
        })
    }

    private fun loadDetails() {
        Global.showProgressDialog(activity)
        apiManager!!.gethomepage(
            ApiEndpoints.gethomepage,
            Global.getPreference(Constant.PREF_TOKEN, "")
        )
        Log.d("PREF_TOKEN", "" + Global.getPreference(Constant.PREF_TOKEN, ""))
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

    internal inner class PagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        override fun getCount(): Int {
            return todayFestivalItemArrayList!!.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(activity)
                .inflate(R.layout.custom_today_festival_item, container, false)
            val festivalItem = todayFestivalItemArrayList!![position]
            val ivphoto = view.findViewById<ImageView>(R.id.ivphoto)
            val tvheading = view.findViewById<TextView>(R.id.tvheading)
            val tvdescription = view.findViewById<TextView>(R.id.tvdescription)
            val tvname = view.findViewById<TextView>(R.id.tvname)
            if (festivalItem.festImage != null && !festivalItem.festImage.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                Glide.with(activity!!).load(festivalItem.festImage).into(ivphoto)
            }
            tvheading.text = festivalItem.festName
            tvdescription.text = "" + festivalItem.festDay
            tvname.text = festivalItem.festDate
            val lay_main = view.findViewById<FrameLayout>(R.id.lay_main)
            lay_main.tag = position
            lay_main.setOnClickListener { view ->
                val date = Global.getPreference(Constant.PREF_CURRRENT_DATE, "")
                val day = getCountOfDays(date, festivalItem.festDate)

//                    if (day <= 1 && day >= 0) {
                val index = view.tag as Int
                val f = todayFestivalItemArrayList!![index]
                val detailact = Intent(activity, ChoosePhotoActivity::class.java)
                detailact.putExtra("object", f)
                activity!!.startActivity(detailact)
                /*                  } else {
                                Global.getAlertDialog(getActivity(), "Sorry!!", "This Festival is locked today.This festival photos will open before 24 hours of festival.");
                            }*/
            }
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    internal inner class PagerAdapterNew : RecyclerView.Adapter<PageViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
            var view: View? = null
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_today_festival_item, null)
            return PageViewHolder(view)
        }

        override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
            val festivalItem = todayFestivalItemArrayList!![position]
            if (festivalItem.festImage != null && !festivalItem.festImage.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                Glide.with(activity!!).load(festivalItem.festImage).into(holder.ivphoto)
            }
            holder.tvheading.text = festivalItem.festName
            holder.tvdescription.text = "" + festivalItem.festDay
            holder.tvname.text = festivalItem.festDate
            holder.lay_main.tag = position
            holder.lay_main.setOnClickListener { view ->
                val date = Global.getPreference(Constant.PREF_CURRRENT_DATE, "")
                val day = getCountOfDays(date, festivalItem.festDate)

                //if (day <= 1 && day >= 0) {
                val index = view.tag as Int
                val f = todayFestivalItemArrayList!![index]
                val detailact = Intent(activity, ChoosePhotoActivity::class.java)
                Global.storePreference("category_name", f.festName)
                detailact.putExtra("object", f)
                activity!!.startActivity(detailact)
                /*} else {
                                Global.getAlertDialog(getActivity(), "Sorry!!", "This Festival is locked today.This festival photos will open before 24 hours of festival.");
                            }*/
            }
        }

        override fun getItemCount(): Int {
            return todayFestivalItemArrayList!!.size
        }

        inner class PageViewHolder(itemView: View?) : RecyclerView.ViewHolder(
            itemView!!
        ) {
            var ivphoto: ImageView
            var tvheading: TextView
            var tvdescription: TextView
            var tvname: TextView
            var lay_main: FrameLayout

            init {
                ivphoto = itemView!!.findViewById(R.id.ivphoto)
                tvheading = itemView.findViewById(R.id.tvheading)
                tvdescription = itemView.findViewById(R.id.tvdescription)
                tvname = itemView.findViewById(R.id.tvname)
                lay_main = itemView.findViewById(R.id.lay_main)
            }
        }
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
            if (requestService.equals(ApiEndpoints.gethomepage, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    val status = Global.getPreference(Constant.PREF_LOGOUT, false)
                    if (status) {
                        Global.storePreference(Constant.PREF_TOKEN, "")
                        Global.storePreference(Constant.PREF_LOGIN, false)
                        Global.storePreference(Constant.PREF_CURRENT_BUSINESS, "")
                        Global.storePreference(Constant.PREF_SCORE, "")
                        Global.storePreference(Constant.PREF_LOGOUT, false)
                        val detailAct = Intent(
                            activity, LoginActivity::class.java
                        )
                        detailAct.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        activity!!.startActivity(detailAct)
                        activity!!.finish()
                    } else {
                        if (message == "user not valid") {
                            val intent = Intent(
                                activity, LoginActivity::class.java
                            )
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            activity!!.finish()
                        } else {
                            if (Global.getPreference(Constant.PREF_CURRENT_BUSINESS, "") == "") {
                                val intent = Intent(
                                    activity, AddFirstBusinessActivity::class.java
                                )
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                activity!!.finish()
                            } else {
                                filldata()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    var current_incident_index = 0
    private fun filldata() {
        if (thisMonthFestivalItemArrayList!!.size > 0) {
            val adapter = ThisMonthFestivalAdapter(activity!!, thisMonthFestivalItemArrayList!!)
            rvdata!!.adapter = adapter
            viewall!!.setOnClickListener { }
        }
        Log.d("todaySize", "" + todayFestivalItemArrayList!!.size)
        if (todayFestivalItemArrayList!!.size > 0) {
            viewPager!!.adapter = PagerAdapterNew()
        }
        if (incidentItemArrayList!!.size > 0) {
            layincidents!!.visibility = View.VISIBLE
            val cIncident = incidentItemArrayList!![current_incident_index]
            if (incidentItemArrayList!!.size == 1) {
                tvprev!!.visibility = View.INVISIBLE
                tvnext!!.visibility = View.INVISIBLE
            }
            if (current_incident_index == 0) {
                tvprev!!.visibility = View.INVISIBLE
            } else {
                tvprev!!.visibility = View.VISIBLE
            }
            tvincident!!.text = cIncident.festName
        } else {
            layincidents!!.visibility = View.GONE
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

    var current_date_index = 0
    fun processResponse(responseString: String?) {
        todayFestivalItemArrayList = ArrayList()
        thisMonthFestivalItemArrayList = ArrayList()
        incidentItemArrayList = ArrayList()
        val date: String
        val share_message: String
        status = false
        logout_Status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            if (jsonObject.has("logout")) {
                logout_Status = jsonObject.getBoolean("logout")
                Global.storePreference(Constant.PREF_LOGOUT, logout_Status)
            }
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }
            if (jsonObject.has("current_date")) {
                date = jsonObject.getString("current_date")
                Global.storePreference(Constant.PREF_CURRRENT_DATE, date)
            }
            if (jsonObject.has("share_message")) {
                share_message = jsonObject.getString("share_message")
                Global.storePreference(Constant.PREF_SHARE_MESSAGE, share_message)
            }
            if (jsonObject.has("slider")) {
                val jsonArray = jsonObject.getJSONArray("slider")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), FestivalItem::class.java)
                        if (f.isCurrentDate) {
                            current_date_index = i
                        }
                        todayFestivalItemArrayList!!.add(f)
                    }
                }
            }
            if (jsonObject.has("festival")) {
                val jsonArray = jsonObject.getJSONArray("festival")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), FestivalItem::class.java)
                        thisMonthFestivalItemArrayList!!.add(f)
                    }
                }
            }
            if (jsonObject.has("incidents")) {
                val jsonArray = jsonObject.getJSONArray("incidents")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), FestivalItem::class.java)
                        incidentItemArrayList!!.add(f)
                    }
                }
            }
            incidentItemArrayList1.clear()
            if (jsonObject.has("incidents")) {
                val jsonArray = jsonObject.getJSONArray("incidents")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), IncidentsItem::class.java)
                        incidentItemArrayList1.add(f)
                    }
                    linearLayout!!.layoutManager = LinearLayoutManager(activity)
                    val customFestivalAdapter =
                        CustomFestivalAdapter(activity!!, incidentItemArrayList1)
                    linearLayout!!.adapter = customFestivalAdapter
                }
            }
            val frameListItems = ArrayList<FrameListItem>()
            if (jsonObject.has("frameList")) {
                val jsonArray = jsonObject.getJSONArray("frameList")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), FrameListItem::class.java)
                        frameListItems.add(f)
                    }
                    val gson = Gson()
                    val json = gson.toJson(frameListItems)
                    Global.storePreference(Constant.PREF_FRAME_LIST, json)
                }
            }

            /*ArrayList<PreferenceItem> preferenceItemArrayList=new ArrayList<>();
            if (jsonObject.has("preference"))
            {
                JSONArray jsonArray = jsonObject.getJSONArray("preference");
                if (jsonArray.length() > 0)
                {
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject j = jsonArray.getJSONObject(i);
                        PreferenceItem f = new Gson().fromJson(j.toString(), PreferenceItem.class);
                        preferenceItemArrayList.add(f);

                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(preferenceItemArrayList);
                    Global.storePreference(Constant.PREF_PREF_LIST,json);
                }

            }*/if (jsonObject.has("current_business")) {
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
            if (jsonObject.has("premium")) {
                val premium = jsonObject.getBoolean("premium")
                Global.storePreference(Constant.PREF_PREMIUM, premium)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openAddImageDialog() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                    } else {
                        //showSettingsDialog();
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    fun getCountOfDays(createdDateString: String?, expireDateString: String?): Int {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        var todayWithZeroTime: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(createdDateString)
            expireCovertedDate = dateFormat.parse(expireDateString)
            val today = Date()
            todayWithZeroTime = dateFormat.parse(dateFormat.format(today))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var cYear = 0
        var cMonth = 0
        var cDay = 0
        if (createdConvertedDate!!.after(todayWithZeroTime)) {
            val cCal = Calendar.getInstance()
            cCal.time = createdConvertedDate
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        } else {
            val cCal = Calendar.getInstance()
            cCal.time = todayWithZeroTime
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        }
        val eCal = Calendar.getInstance()
        eCal.time = expireCovertedDate
        val eYear = eCal[Calendar.YEAR]
        val eMonth = eCal[Calendar.MONTH]
        val eDay = eCal[Calendar.DAY_OF_MONTH]
        val date1 = Calendar.getInstance()
        val date2 = Calendar.getInstance()
        date1.clear()
        date1[cYear, cMonth] = cDay
        date2.clear()
        date2[eYear, eMonth] = eDay
        val diff = date2.timeInMillis - date1.timeInMillis
        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
        return dayCount.toInt()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}