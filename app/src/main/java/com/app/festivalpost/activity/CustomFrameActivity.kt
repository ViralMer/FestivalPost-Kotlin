package com.app.festivalpost.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.adapter.ChoosePhotoFrameAdapter
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.*
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.get
import com.emegamart.lelys.utils.extensions.launchActivity
import com.emegamart.lelys.utils.extensions.toast
import kotlinx.android.synthetic.main.fragment_custom.*
import java.util.*

class CustomFrameActivity : AppBaseActivity(), OnItemClickListener {

    var rvdata: RecyclerView? = null
    var customCategoryPostItem = arrayListOf<CustomCategoryPostItem>()
    private var layroot: LinearLayout? = null
    private var ivbackground: ImageView? = null
    var horizontalLayoutManagaer: GridLayoutManager? = null
    var title: String? = null
    var custom_category_id: String? = null
    var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_frame)
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        setActionbar()
        horizontalLayoutManagaer = GridLayoutManager(this@CustomFrameActivity, 4)
        rvdata!!.layoutManager = horizontalLayoutManagaer
        rvdata!!.itemAnimator = DefaultItemAnimator()
        custom_category_id = intent.getStringExtra("custom_category_id")
        loadDetails()


    }


    var tvaction: TextView? = null
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
            tvtitle.text = "Select Greetings"
        }
        tvaction = toolbar.findViewById<View>(R.id.btn_next) as TextView
        tvaction!!.text = resources.getString(R.string.txt_next)

        tvaction!!.setOnClickListener {
            showProgress(true)
            val currentBusinessItem =
                get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS)
            if (currentBusinessItem == null) {
                val materialAlertDialogBuilder = AlertDialog.Builder(this)
                val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.custom_add_busines_dialog, null)
                val tvTitle: TextView
                val tvMessage: TextView
                val btnOk: Button
                val btnCancel: Button
                tvTitle = view.findViewById(R.id.tvTitle)
                tvMessage = view.findViewById(R.id.tvMessage)
                btnOk = view.findViewById(R.id.btnOk)
                btnCancel = view.findViewById(R.id.btnCancel)
                tvTitle.text = "Sorry!!"
                tvMessage.text = "For making post please add your business details first."
                materialAlertDialogBuilder.setView(view).setCancelable(true)
                val b = materialAlertDialogBuilder.create()
                btnCancel.setOnClickListener { b.dismiss() }
                btnOk.setOnClickListener { launchActivity<AddBusinessActivity> { finish() } }
                b.show()
                showProgress(false)
            } else {
                val detailact =
                    Intent(this@CustomFrameActivity, CustomPhotoFrameActivity::class.java)
                detailact.putExtra("photo_path", photo_path)
                Log.d("photo_path", "123 " + photo_path)
                detailact.putExtra("frame_contact_detail", frameContentItemDetail)
                startActivity(detailact)
                showProgress(false)
            }
        }
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


    private var photo_path: String? = ""
    var frameContentItemDetail: CustomCategoryPostItem? = null
    override fun onItemClicked(`object`: Any?, index: Int) {
        val photoItem = `object` as CustomCategoryPostItem
        frameContentItemDetail = `object` as CustomCategoryPostItem
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

    private fun loadDetails() {

        showProgress(true)
        callApi(

            getRestApis().getLanguageCustomeCategoryPost(custom_category_id!!), onApiSuccess = {
                showProgress(false)
                customCategoryPostItem = it.data

                if (customCategoryPostItem.size > 0) {
                    val adapter =
                        ChoosePhotoFrameAdapter(this@CustomFrameActivity, customCategoryPostItem)
                    rvdata!!.adapter = adapter

                    val photoItem = customCategoryPostItem[0]
                    frameContentItemDetail=customCategoryPostItem[0]
                    photo_path = photoItem.banner_image!!
                    photoItem.is_selected=true

                    Log.d("photo_path",""+photo_path)
                    Glide.with(this).load(photoItem.image)
                        .placeholder(R.drawable.placeholder_img).error(
                            R.drawable.placeholder_img
                        ).into(
                            ivbackground!!
                        )
                    Handler().postDelayed({ // do something after 2s = 2000 miliseconds
                        rvdata!!.smoothScrollToPosition(0)
                    }, 2000)
                } else {
                    toast("No Data Found")
                }


            }, onApiError = {
                showProgress(false)

            }, onNetworkError = {
                showProgress(false)

            })
    }
}