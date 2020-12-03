package com.app.festivalpost

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.adapter.ChoosePhotoAdapter
import com.app.festivalpost.adapter.FrameAdapter

import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.apifunctions.ApiManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.models.FestivalItem
import com.app.festivalpost.models.IncidentsItem
import com.app.festivalpost.models.PostContentItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.PhotoItem
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChoosePhotoActivity : AppCompatActivity(), ApiResponseListener, OnItemClickListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var rvdata: RecyclerView? = null
    var festivalItem: FestivalItem? = null
    var postContentItem: IncidentsItem? = null
    var postContentItem1: PostContentItem? = null
    private var layroot: LinearLayout? = null
    private var ivbackground: ImageView? = null
    private var day = 0
    var horizontalLayoutManagaer: GridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_photo)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setActionbar()
        apiManager = ApiManager(this@ChoosePhotoActivity)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("object")) {
                festivalItem = bundle["object"] as FestivalItem?
                val date = Global.getPreference(Constant.PREF_CURRRENT_DATE, "")
                day = getCountOfDays(date, festivalItem!!.festDate)
            } else if (bundle.containsKey("object_post")) {
                postContentItem = bundle["object_post"] as IncidentsItem?
                val date = Global.getPreference(Constant.PREF_CURRRENT_DATE, "")
                day = 0
            } else if (bundle.containsKey("object_post_content")) {
                postContentItem1 = bundle["object_post_content"] as PostContentItem?
                val date = Global.getPreference(Constant.PREF_CURRRENT_DATE, "")
                day = 0
            }
        }
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        horizontalLayoutManagaer = GridLayoutManager(this@ChoosePhotoActivity, 4)
        rvdata!!.layoutManager = horizontalLayoutManagaer
        rvdata!!.itemAnimator = DefaultItemAnimator()
        Global.showProgressDialog(this@ChoosePhotoActivity)
        if (bundle != null) {
            if (bundle.containsKey("object")) {
                apiManager!!.getfestivalimages(
                    ApiEndpoints.getfestivalimages,
                    festivalItem!!.festId
                )
            } else if (bundle.containsKey("object_post")) {
                apiManager!!.getfestivalimages(
                    ApiEndpoints.getfestivalimages,
                    postContentItem!!.fest_id
                )
            } else if (bundle.containsKey("object_post_content")) {
                apiManager!!.getfestivalimages(
                    ApiEndpoints.getfestivalimages,
                    postContentItem1!!.fest_id
                )
            }
        }
        val t: Thread = object : Thread() {
            override fun run() {
                while (!isInterrupted) {
                    try {
                        sleep(5000) //1000ms = 1 sec
                        runOnUiThread { animateButton() }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        t.start()
    }

    var photoItemArrayList = ArrayList<PhotoItem>()
    fun fillData() {
        /*if (photoItemArrayList.size()>3)
        {
            horizontalLayoutManagaer.setStackFromEnd(true);
        }*/
        if (photoItemArrayList.size > 0) {
            val adapter = ChoosePhotoAdapter(this@ChoosePhotoActivity, photoItemArrayList)
            rvdata!!.adapter = adapter
            val index = Global.getPreference(Constant.PREF_PHOTO_INDEX, 0)
            Log.d("INDEXSIZe", "" + index)
            val photoItem = photoItemArrayList[index]
            if (!photoItem.post_content.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                photo_path = photoItem.post_content
                Glide.with(this@ChoosePhotoActivity).load(photoItem.post_content)
                    .placeholder(R.drawable.placeholder_img).error(
                        R.drawable.placeholder_img
                    ).into(
                        ivbackground!!
                    )
                Handler().postDelayed({ // do something after 2s = 2000 miliseconds
                    rvdata!!.smoothScrollToPosition(0)
                }, 2000) //Time in milisecond
            }
        } else {
            Global.showFailDialog(this@ChoosePhotoActivity, message)
        }
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

    var tvaction: TextView? = null
    override fun onResume() {
        super.onResume()
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        if (Global.getPreference("category_name", "") == "") {
            tvtitle.text = resources.getString(R.string.txt_custom_photo)
        } else {
            tvtitle.text = Global.getPreference("category_name", "")
        }
        tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvaction!!.text = resources.getString(R.string.txt_next)
        animateButton()
        tvaction!!.setOnClickListener {
            if (day <= 1 && day >= 0) {
                val detailact =
                    Intent(this@ChoosePhotoActivity, ChooseFrameForPhotoActivityNew::class.java)
                detailact.putExtra("photo_path", photo_path)
                startActivity(detailact)
            } else {
                Global.getAlertDialog(
                    this@ChoosePhotoActivity,
                    "Sorry!!",
                    "This Festival is locked today.This festival photos will open before 24 hours of festival."
                )
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ChoosePhotoActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@ChoosePhotoActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ChoosePhotoActivity)
            if (requestService.equals(ApiEndpoints.getfestivalimages, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        fillData()
                    } else {
                        Toast.makeText(this@ChoosePhotoActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            animateButton()
        }
    }

    fun animateButton() {
        val myAnim = AnimationUtils.loadAnimation(this@ChoosePhotoActivity, R.anim.bounce)
        val interpolator = com.app.festivalpost.MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        tvaction!!.startAnimation(myAnim)
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ChoosePhotoActivity)
            Global.showFailDialog(this@ChoosePhotoActivity, responseString)
        }
    }

    fun processResponse(responseString: String?) {
        photoItemArrayList = ArrayList()
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
            if (jsonObject.has("data")) {
                val jsonArray = jsonObject.getJSONArray("data")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), PhotoItem::class.java)
                        val index = Global.getPreference(Constant.PREF_PHOTO_INDEX, 0)
                        Log.d("Index123", "" + index)
                        if (i == index) {
                            f.setIs_selected(true)
                        } else {
                            f.setIs_selected(false)
                        }
                        photoItemArrayList.add(f)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var photo_path = ""
    override fun onItemClicked(`object`: Any, index: Int) {
        val photoItem = `object` as PhotoItem
        if (photoItem.post_content != null && !photoItem.post_content.equals(
                "",
                ignoreCase = true
            )
        ) {
            photo_path = photoItem.post_content
            Glide.with(this@ChoosePhotoActivity).load(photoItem.post_content)
                .placeholder(R.drawable.placeholder_img).error(
                    R.drawable.placeholder_img
                ).into(
                    ivbackground!!
                )
        }
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
}