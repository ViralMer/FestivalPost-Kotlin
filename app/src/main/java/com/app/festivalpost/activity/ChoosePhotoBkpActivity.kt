package com.app.festivalpost.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.app.festivalpost.GridSpacingItemDecoration
import com.app.festivalpost.OnItemClickListener
import com.app.festivalpost.R
import com.app.festivalpost.SaveAndShareActivity
import com.app.festivalpost.adapter.ChoosePhotoAdapter
import com.app.festivalpost.adapter.LocalFrameAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.BusinessItem
import com.app.festivalpost.models.FestivalItem
import com.app.festivalpost.models.PhotoItem
import com.app.festivalpost.photoeditor.PhotoEditor
import com.app.festivalpost.photoeditor.PhotoEditorView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class ChoosePhotoBkpActivity : AppCompatActivity(), ApiResponseListener, OnItemClickListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var rvdata: RecyclerView? = null
    var festivalItem: FestivalItem? = null
    private var layroot: LinearLayout? = null
    private var ivbackground: ImageView? = null
    private var photoEditorView: PhotoEditorView? = null
    var mPhotoEditor: PhotoEditor? = null
    var llframe: LinearLayout? = null
    var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_photo_bkp)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setActionbar()
        apiManager = ApiManager(this@ChoosePhotoBkpActivity)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("object")) {
                festivalItem = bundle["object"] as FestivalItem?
            }
        }
        viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        llframe = findViewById<View>(R.id.llframe) as LinearLayout
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        photoEditorView = findViewById<View>(R.id.photoEditorView) as PhotoEditorView
        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView)
            .setPinchTextScalable(true)
            .build()
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        val mLayoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(this@ChoosePhotoBkpActivity, 3)
        rvdata!!.layoutManager = mLayoutManager
        rvdata!!.addItemDecoration(GridSpacingItemDecoration(3, dpToPx(12), true))
        rvdata!!.itemAnimator = DefaultItemAnimator()
        Global.showProgressDialog(this@ChoosePhotoBkpActivity)
        apiManager!!.getfestivalimages(ApiEndpoints.getfestivalimages, festivalItem!!.festId)
    }

    var photoItemArrayList = ArrayList<PhotoItem>()
    fun fillData() {
        if (photoItemArrayList.size > 0) {
            val adapter = ChoosePhotoAdapter(this@ChoosePhotoBkpActivity, photoItemArrayList)
            rvdata!!.adapter = adapter
        } else {
            Global.showFailDialog(this@ChoosePhotoBkpActivity, message)
        }

//        apiManager.getmycurrentbusiness(ApiEndpoints.getmycurrentbusiness, Global.getPreference(Constant.PREF_TOKEN, ""));
    }

    fun fillFrames(businessItem: BusinessItem?) {
        val localFrameItemArrayList = Global.allFrames
        if (localFrameItemArrayList.size > 0) {
            val frameAdapter = LocalFrameAdapter(
                this@ChoosePhotoBkpActivity,
                localFrameItemArrayList,
                businessItem
            )
            viewPager!!.adapter = frameAdapter
            val f = localFrameItemArrayList[0]
            if (llframe!!.childCount > 0) llframe!!.removeAllViews()
            val frame_view = LayoutInflater.from(this@ChoosePhotoBkpActivity).inflate(
                f.layout_id,
                llframe, false
            )
            val ivframelogo = frame_view.findViewById<ImageView>(R.id.ivframelogo)
            val tvframephone = frame_view.findViewById<TextView>(R.id.tvframephone)
            val tvframeemail = frame_view.findViewById<TextView>(R.id.tvframeemail)
            val tvframeweb = frame_view.findViewById<TextView>(R.id.tvframeweb)
            if (businessItem != null) {
                if (businessItem.busiLogo != "") {
                    Glide.with(this@ChoosePhotoBkpActivity).load(businessItem.busiLogo).placeholder(
                        R.drawable.placeholder_img
                    ).error(R.drawable.placeholder_img).into(ivframelogo)
                }
                tvframephone.text = businessItem.busiMobile
                tvframeemail.text = businessItem.busiEmail
                tvframeweb.text = businessItem.busiWebsite
            }
            llframe!!.addView(frame_view)
            viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
//                    if (ivframe.getVisibility() == View.VISIBLE) {
//                        ivframe.setVisibility(View.GONE);
//                    }
                }

                override fun onPageSelected(position: Int) {
//                    ivframe.setVisibility(View.VISIBLE);
                    val f = localFrameItemArrayList[position]
                    if (llframe!!.childCount > 0) llframe!!.removeAllViews()
                    val frame_view = LayoutInflater.from(this@ChoosePhotoBkpActivity).inflate(
                        f.layout_id,
                        llframe, false
                    )
                    val ivframelogo = frame_view.findViewById<ImageView>(R.id.ivframelogo)
                    val tvframephone = frame_view.findViewById<TextView>(R.id.tvframephone)
                    val tvframeemail = frame_view.findViewById<TextView>(R.id.tvframeemail)
                    val tvframeweb = frame_view.findViewById<TextView>(R.id.tvframeweb)
                    if (businessItem != null) {
                        if (businessItem.busiLogo != "") {
                            Glide.with(this@ChoosePhotoBkpActivity).load(businessItem.busiLogo)
                                .placeholder(
                                    R.drawable.placeholder_img
                                ).error(R.drawable.placeholder_img).into(ivframelogo)
                        }
                        tvframephone.text = businessItem.busiMobile
                        tvframeemail.text = businessItem.busiEmail
                        tvframeweb.text = businessItem.busiWebsite
                    }
                    llframe!!.addView(frame_view)
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
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

    fun pxtoSdp(context: Context, value: Int): Int {
        var valueInPixels = 0
        for (i in 0..299) {
            if (i == value) {
                val q = context.resources.getDimension(i).toString().toInt()
                valueInPixels = context.resources.getDimensionPixelOffset(q)
                return valueInPixels
            }
        }
        return valueInPixels
    }

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
        tvtitle.text = resources.getString(R.string.txt_choose_photo)
        val tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvaction.text = resources.getString(R.string.txt_next)
        tvaction.setOnClickListener { CreateImageAsync().execute() }
    }

    internal inner class CreateImageAsync : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Global.showProgressDialog(this@ChoosePhotoBkpActivity)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            mPhotoEditor!!.clearHelperBox()
            layroot!!.isDrawingCacheEnabled = true
            layroot!!.buildDrawingCache(true)
            val savedBmp = Bitmap.createBitmap(layroot!!.drawingCache)
            layroot!!.isDrawingCacheEnabled = false
            try {
                //Write file
                val filename = "bitmap.png"
                val stream = openFileOutput(filename, MODE_PRIVATE)
                savedBmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
                //Cleanup
                stream.close()
                savedBmp.recycle()
                Global.dismissProgressDialog(this@ChoosePhotoBkpActivity)
                //Pop intent
                val in1 = Intent(this@ChoosePhotoBkpActivity, SaveAndShareActivity::class.java)
                in1.putExtra("image", filename)
                startActivity(in1)
            } catch (e: Exception) {
                e.printStackTrace()
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
            Global.dismissProgressDialog(this@ChoosePhotoBkpActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@ChoosePhotoBkpActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ChoosePhotoBkpActivity)
            if (requestService.equals(ApiEndpoints.getfestivalimages, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        fillData()
                    } else {
                        Toast.makeText(this@ChoosePhotoBkpActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

//                if (requestService.equalsIgnoreCase(ApiEndpoints.getmycurrentbusiness)) {
//                    try {
//
//                        Log.d("response", responseString);
//                        processBusinessResponse(responseString);
//
//                        if (status) {
//                            fillFrames(businessItem);
//                        } else {
//                            Global.showFailDialog(ChoosePhotoBkpActivity.this, message);
//                        }
//                    } catch (Exception e) {
//
//                    }
//                }
        }
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ChoosePhotoBkpActivity)
            Global.showFailDialog(this@ChoosePhotoBkpActivity, responseString)
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
                        photoItemArrayList.add(f)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var premium = false
    var businessItem: BusinessItem? = null
    fun processBusinessResponse(responseString: String?) {
        businessItem = null
        status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            if (jsonObject.has("premium")) {
                premium = jsonObject.getBoolean("premium")
            }
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }
            if (status) {
                if (jsonObject.has("data")) {
                    val businessJsonObject = jsonObject.getJSONObject("data")
                    businessItem =
                        Gson().fromJson(businessJsonObject.toString(), BusinessItem::class.java)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onItemClicked(`object`: Any, index: Int) {
        val photoItem = `object` as PhotoItem
        if (photoItem.post_content != null && !photoItem.post_content.equals(
                "",
                ignoreCase = true
            )
        ) {
            Glide.with(this@ChoosePhotoBkpActivity).load(photoItem.post_content)
                .placeholder(R.drawable.placeholder_img).error(
                    R.drawable.placeholder_img
                ).into(
                    ivbackground!!
                )
        }
    }
}