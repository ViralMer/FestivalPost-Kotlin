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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.app.festivalpost.R
import com.app.festivalpost.activity.FestivalViewAllActivitty
import com.app.festivalpost.activity.FestivalViewAllVideoActivity
import com.app.festivalpost.activity.LoginActivity
import com.app.festivalpost.adapter.CategoryItemAdapter
import com.app.festivalpost.adapter.CustomFestivalItemAdapter
import com.app.festivalpost.adapter.CustomFestivalVideoItemAdapter
import com.app.festivalpost.adapter.VideoCategoryItemAdapter
import com.app.festivalpost.models.*
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_video.*

@Suppress("DEPRECATION")
class VideoFragment : BaseFragment() {

    private var festivalVideoArrayList = arrayListOf<VideoPageItem?>()
    private var categoryVideoArrayList = arrayListOf<VideoPageItem?>()
    private var rcvCustomFestivalVideo: RecyclerView? = null
    private var rcvCustomCategoryVideo: RecyclerView? = null
    private var linearCategoryVideo: LinearLayout? = null
    private var linearFestivalVideo: LinearLayout? = null

    private var sessionManager: SessionManager? = null



    var token : String?=null

    private var tvviewall: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_video, container, false)
        openAddImageDialog()

        rcvCustomCategoryVideo = view.findViewById(R.id.customCategoryVideo)
        rcvCustomFestivalVideo = view.findViewById(R.id.customFestivalVideo)
        linearCategoryVideo = view.findViewById(R.id.linearCategoryVideo)
        linearFestivalVideo = view.findViewById(R.id.linearFestivalVideo)
        sessionManager= SessionManager(activity!!)
        token=sessionManager!!.getValueString(Constants.SharedPref.USER_TOKEN)
        tvviewall = view.findViewById(R.id.tvviewall)
        tvviewall!!.onClick {
            activity!!.launchActivity<FestivalViewAllVideoActivity> {  }
        }
        loadVideoPageData()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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


    private fun loadVideoPageData() {
        showProgress()
        callApi(
            getRestApis().getVideoPageData(token!!),
            onApiSuccess = { res ->
                hideProgress()
                if (res.status!!) {

                    festivalVideoArrayList = res.festival
                    categoryVideoArrayList = res.category

                    rcvCustomFestivalVideo!!.layoutManager = LinearLayoutManager(
                        activity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val customFestivalAdapter =
                        CustomFestivalVideoItemAdapter(activity!!, festivalVideoArrayList)
                    rcvCustomFestivalVideo!!.adapter = customFestivalAdapter

                    rcvCustomCategoryVideo!!.layoutManager = GridLayoutManager(activity, 3)
                    val customCategoryAdapter =
                        VideoCategoryItemAdapter(activity!!, categoryVideoArrayList)
                    rcvCustomCategoryVideo!!.adapter = customCategoryAdapter

                    if (festivalVideoArrayList.size > 0) {
                        linearFestivalVideo!!.show()
                    } else {
                        linearFestivalVideo!!.hide()

                    }

                    if (categoryVideoArrayList.size > 0) {
                        linearCategoryVideo!!.show()
                    } else {
                        linearCategoryVideo!!.hide()
                    }


                }
            },
            onApiError = {
                if (activity == null) return@callApi
                hideProgress()


            },
            onNetworkError = {
                if (activity == null) return@callApi
                hideProgress()
                toast("Please Connect your network")
            })
    }


}