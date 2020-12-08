package com.app.festivalpost.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.CustomPhotoFrameActivity
import com.app.festivalpost.activity.MyBounceInterpolator
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.R
import com.app.festivalpost.adapter.ChoosePhotoFrameAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.FrameContentItem
import com.app.festivalpost.models.FrameContentItemDetail
import com.app.festivalpost.models.FrameContentListItem
import com.app.festivalpost.models.SliderItem
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class CustomFrameActivity : AppCompatActivity(), ApiResponseListener,OnItemClickListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var rvdata: RecyclerView? = null
    var frameContentListItem: FrameContentListItem? = null
    var frameContentItem: FrameContentItem? = null
    var sliderItem: SliderItem? = null
    private var layroot: LinearLayout? = null
    private var ivbackground: ImageView? = null
    var horizontalLayoutManagaer: GridLayoutManager? = null
    var title: String? = null
    var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_frame)
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        apiManager = ApiManager(this)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("object_post")) {
                title = bundle.getString("custom_category_name")
                index = bundle.getInt("index")
                Log.d("StringTitel", "" + title + "Index Is:" + index)
                frameContentListItem = bundle["object_post"] as FrameContentListItem?
            } else if (bundle.containsKey("object_post_content")) {
                frameContentItem = bundle["object_post_content"] as FrameContentItem?
                title = bundle.getString("custom_category_name")
                index = bundle.getInt("index")
                Log.d("StringTitel", "" + title + "Index Is:" + index)
            }
            /*else if (bundle.containsKey("object_slider"))
            {
                sliderItem=(SliderItem) bundle.get("object_slider");
            }*/
        }
        setActionbar()
        horizontalLayoutManagaer = GridLayoutManager(this@CustomFrameActivity, 4)
        rvdata!!.layoutManager = horizontalLayoutManagaer
        rvdata!!.itemAnimator = DefaultItemAnimator()
        Global.showProgressDialog(this@CustomFrameActivity)
        if (bundle != null) {
            if (bundle.containsKey("object_post")) {
                apiManager!!.getcustomframeimages(
                    ApiEndpoints.getCustomCategoryImages, Global.getPreference(
                        Constant.PREF_TOKEN, ""
                    ), frameContentListItem!!.custom_cateogry_id.toString()
                )
            } else if (bundle.containsKey("object_post_content")) {
                apiManager!!.getcustomframeimages(
                    ApiEndpoints.getCustomCategoryImages, Global.getPreference(
                        Constant.PREF_TOKEN, ""
                    ), frameContentItem!!.custom_cateogry_id
                )
            }
            /*   else if (bundle.containsKey("object_slider"))
            {
                apiManager.getcustomframeimages(ApiEndpoints.getCustomCategoryImages,Global.getPreference(Constant.PREF_TOKEN,""), String.valueOf(sliderItem.getCustom_cateogry_id()));
            }*/
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
        if (title != null && title == "") {
            tvtitle.text = resources.getString(R.string.txt_choose_photo)
        } else {
            tvtitle.text = title
        }
        tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvaction!!.text = resources.getString(R.string.txt_next)
        animateButton()
        tvaction!!.setOnClickListener {
            val detailact = Intent(this@CustomFrameActivity, CustomPhotoFrameActivity::class.java)
            detailact.putExtra("photo_path", photo_path)
            detailact.putExtra("frame_contact_detail", frameContentItemDetail)
            startActivity(detailact)
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
            Global.dismissProgressDialog(this@CustomFrameActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@CustomFrameActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@CustomFrameActivity)
            if (requestService.equals(ApiEndpoints.getCustomCategoryImages, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        fillData()
                    } else {
                        Toast.makeText(this@CustomFrameActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            animateButton()
        }
    }

    fun animateButton() {
        val myAnim = AnimationUtils.loadAnimation(this@CustomFrameActivity, R.anim.bounce)
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        tvaction!!.startAnimation(myAnim)
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@CustomFrameActivity)
            Global.showFailDialog(this@CustomFrameActivity, responseString)
        }
    }

    var frameContentItemDetailArrayList = ArrayList<FrameContentItemDetail>()
    fun processResponse(responseString: String?) {
        frameContentItemDetailArrayList = ArrayList()
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
                        val f = Gson().fromJson(j.toString(), FrameContentItemDetail::class.java)
                        val frameContentItemDetail = FrameContentItemDetail()
                        frameContentItemDetail.banner_image = j.getString("banner_image")
                        frameContentItemDetail.custom_cateogry_data_id =
                            j.getInt("custom_cateogry_data_id")
                        frameContentItemDetail.custom_cateogry_id = j.getInt("custom_cateogry_id")
                        frameContentItemDetail.image_one = j.getString("image_one")
                        frameContentItemDetail.img_height = j.getString("img_height")
                        frameContentItemDetail.img_width = j.getString("img_width")
                        frameContentItemDetail.img_position_x = j.getString("img_position_x")
                        frameContentItemDetail.img_position_y = j.getString("img_position_y")
                        frameContentItemDetail.position_x = j.getString("position_x")
                        frameContentItemDetail.position_y = j.getString("position_y")
                        if (i == index) {
                            f.setIs_selected(true)
                        } else {
                            f.setIs_selected(false)
                        }
                        frameContentItemDetailArrayList.add(frameContentItemDetail)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fillData() {
        /*if (frameContentItemDetailArrayList.size()>3)
        {
            horizontalLayoutManagaer.setStackFromEnd(true);
        }*/
        if (frameContentItemDetailArrayList.size > 0) {
            val adapter =
                ChoosePhotoFrameAdapter(this@CustomFrameActivity, frameContentItemDetailArrayList)
            rvdata!!.adapter = adapter
            frameContentItemDetail = frameContentItemDetailArrayList[index]
            val photoItem = frameContentItemDetailArrayList[index]
            if (photoItem.banner_image != null && !photoItem.banner_image.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                photo_path = photoItem.banner_image
                Glide.with(this@CustomFrameActivity).load(photoItem.banner_image)
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
            Global.showFailDialog(this@CustomFrameActivity, message)
        }
    }

    var photo_path: String? = ""
    var frameContentItemDetail: FrameContentItemDetail? = null
    override fun onItemClicked(`object`: Any?, index: Int) {
        val photoItem = `object` as FrameContentItemDetail
        frameContentItemDetail = `object`
        if (photoItem.banner_image != null && !photoItem.banner_image.equals(
                "",
                ignoreCase = true
            )
        ) {
            photo_path = photoItem.banner_image
            Glide.with(this@CustomFrameActivity).load(photoItem.banner_image)
                .placeholder(R.drawable.placeholder_img).error(
                    R.drawable.placeholder_img
                ).into(
                    ivbackground!!
                )
        }
    }
}