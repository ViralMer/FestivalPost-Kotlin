package com.app.festivalpost.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper.getMainLooper
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.app.festivalpost.R
import com.app.festivalpost.activity.LoginActivity
import com.app.festivalpost.adapter.CategoryItemAdapter
import com.app.festivalpost.adapter.CustomFestivalItemAdapter
import com.app.festivalpost.models.*
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.app.festivalpost.utils.Constants.KeyIntent.CURRENT_DATE
import com.app.festivalpost.utils.Constants.KeyIntent.IS_PREMIUM
import com.app.festivalpost.utils.Constants.KeyIntent.LOG_OUT
import com.app.festivalpost.utils.Constants.SharedPref.KEY_CURRENT_BUSINESS
import com.app.festivalpost.utils.Constants.SharedPref.KEY_FRAME_LIST
import com.app.festivalpost.utils.Constants.SharedPref.USER_TOKEN
import com.emegamart.lelys.utils.extensions.getSharedPrefInstance
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.loadImageFromUrl
import com.emegamart.lelys.utils.extensions.show
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File
import java.io.FileOutputStream

@Suppress("DEPRECATION")
class HomeFragment : BaseFragment() {

    private var sliderArrayList = arrayListOf<HomePageItem?>()
    private var festivalArrayList = arrayListOf<HomePageItem?>()
    private var categoryArrayList = arrayListOf<HomePageItem?>()
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var viewPager: ViewPager? = null
    private var rcvCustomFestival: RecyclerView? = null
    private var rcvCustomCategory: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openAddImageDialog()

        rcvCustomCategory = view.findViewById(R.id.customCategory)
        rcvCustomFestival = view.findViewById(R.id.customFestival)
        mSwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)
        viewPager = view.findViewById(R.id.sliderviewPager)
        //loadHomePageData()
        mSwipeRefreshLayout!!.setOnRefreshListener {
            loadHomePageData()
            mSwipeRefreshLayout!!.isRefreshing = false
        }

        val mainHandler = Handler(getMainLooper())
        var runnable: Runnable = object : Runnable {
            override fun run() {
                loadHomePageData()
            }

        }

        mainHandler.postDelayed(runnable, 0)


    }


    inner class PagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        override fun getCount(): Int {
            return sliderArrayList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_slider, container, false)
            val ivphoto = view.findViewById<ImageView>(R.id.imgSlider)
            val festivalItem = sliderArrayList[position]
            if (festivalItem!!.fest_image != null && !festivalItem.fest_image.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                ivphoto.loadImageFromUrl(festivalItem.fest_image!!)
            }

            container.addView(view)
            return view
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }


    private fun openAddImageDialog() {
        Dexter.withContext(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }


    private fun loadHomePageData() {
        showProgress()
        val token = getString(R.string.token)
        getSharedPrefInstance().setValue(USER_TOKEN, token)
        callApi(
            getRestApis().getHomePageData(),
            onApiSuccess = { res ->
                hideProgress()
                if (res.status!!) {


                    getSharedPrefInstance().setValue(LOG_OUT, res.logout)
                    getSharedPrefInstance().setValue(IS_PREMIUM, res.premium)
                    getSharedPrefInstance().setValue(CURRENT_DATE, res.current_date)
                    getSharedPrefInstance().setValue(KEY_FRAME_LIST, res.frameList)
                    getSharedPrefInstance().setValue(KEY_CURRENT_BUSINESS, res.current_business)


                    sliderArrayList = res.slider
                    festivalArrayList = res.festival
                    categoryArrayList = res.cateogry

                    rcvCustomFestival!!.layoutManager = LinearLayoutManager(
                        activity,
                        LinearLayoutManager.HORIZONTAL,
                        true
                    )
                    val customFestivalAdapter =
                        CustomFestivalItemAdapter(activity!!, festivalArrayList)
                    rcvCustomFestival!!.adapter = customFestivalAdapter

                    rcvCustomCategory!!.layoutManager = GridLayoutManager(activity, 3)
                    val customCategoryAdapter =
                        CategoryItemAdapter(activity!!, categoryArrayList)
                    rcvCustomCategory!!.adapter = customCategoryAdapter

                    if (sliderArrayList.size > 0) {
                        viewPager!!.adapter = PagerAdapter()
                    }

                    if (festivalArrayList.size > 0) {
                        linearFestival.show()
                    } else {
                        linearFestival.hide()

                    }

                    if (categoryArrayList.size > 0) {
                        linearCategory.show()
                    } else {
                        linearCategory.hide()
                    }






                    Glide.with(this).asBitmap().load(res.current_business.busi_logo)
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



                    if (res.logout!!) {
                        val detailAct = Intent(
                            activity, LoginActivity::class.java
                        )
                        detailAct.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        activity!!.startActivity(detailAct)
                        activity!!.finish()
                    } else {
                        if (res.message == "user not valid") {
                            val intent = Intent(
                                activity, LoginActivity::class.java
                            )
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            activity!!.finish()
                        }
                    }


                } else {
                    linearFestival.hide()
                    linearCategory.hide()
                }
            },
            onApiError = {
                if (activity == null) return@callApi
                hideProgress()
                linearCategory.hide()
                linearFestival.hide()

            },
            onNetworkError = {
                if (activity == null) return@callApi
                hideProgress()
            })
    }


}