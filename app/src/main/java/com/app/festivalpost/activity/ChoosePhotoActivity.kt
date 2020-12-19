package com.app.festivalpost.activity;

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.adapter.ChoosePhotoAdapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.*
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.*
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.getCurrentDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChoosePhotoActivity : AppBaseActivity(), OnItemClickListener {
    var rvdata: RecyclerView? = null
    var layroot: LinearLayout? = null
    var ivbackground: ImageView? = null


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
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        horizontalLayoutManagaer = GridLayoutManager(this@ChoosePhotoActivity, 4)
        rvdata!!.layoutManager = horizontalLayoutManagaer
        rvdata!!.itemAnimator = DefaultItemAnimator()
        loadCategoryImages()
        if (intent.getStringExtra("category_date") != null) {
            day = getCountOfDays(getCurrentDate(), intent.getStringExtra("category_date"))
        }


    }

    var photoItemArrayList = arrayListOf<CategoryItem?>()


    var tvaction: TextView? = null

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        if (intent.getStringExtra("category_name") == "") {
            tvtitle.text = resources.getString(R.string.txt_custom_photo)
        } else {
            tvtitle.text = intent.getStringExtra("category_name")
        }
        tvaction = toolbar.findViewById<View>(R.id.btn_next) as TextView
        tvaction!!.text = resources.getString(R.string.txt_next)
        animateButton()
        tvaction!!.setOnClickListener {
            if (day in 0..1) {
                val detailact = Intent(this@ChoosePhotoActivity, ChooseFrameForPhotoActivityNew::class.java)
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
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun animateButton() {
        val myAnim = AnimationUtils.loadAnimation(this@ChoosePhotoActivity, R.anim.bounce)
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        tvaction!!.startAnimation(myAnim)
    }


    private var photo_path = ""
    override fun onItemClicked(`object`: Any?, index: Int) {
        val photoItem = `object` as PhotoItem
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
        }
    }

    private fun getCountOfDays(createdDateString: String?, expireDateString: String?): Int {
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


    private fun loadCategoryImages() {
        showProgress(true)


        callApi(
            getRestApis().getCategoryImages(intent.getStringExtra("category_id")!!),
            onApiSuccess = { res ->
                showProgress(false)
                if (res.status!!) {
                    photoItemArrayList = arrayListOf()
                    photoItemArrayList = res.data

                    if (photoItemArrayList.isNotEmpty()) {
                        val adapter =
                            ChoosePhotoAdapter(this@ChoosePhotoActivity, photoItemArrayList)
                        rvdata!!.adapter = adapter
                        val photoItem = photoItemArrayList[0]

                        photo_path = photoItem!!.post_content!!
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


                }
            },
            onApiError = {
                showProgress(false)


            },
            onNetworkError = {
                showProgress(false)
            })
    }
}