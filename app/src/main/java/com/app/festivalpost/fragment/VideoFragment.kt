package com.app.festivalpost.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.R
import com.app.festivalpost.activity.FestivalViewAllVideoActivity
import com.app.festivalpost.adapter.CustomFestivalVideoItemAdapter
import com.app.festivalpost.adapter.VideoCategoryItemAdapter
import com.app.festivalpost.api.RestClient
import com.app.festivalpost.models.VideoPageItem
import com.app.festivalpost.models.VideoPageResponse
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.launchActivity
import com.emegamart.lelys.utils.extensions.onClick
import com.emegamart.lelys.utils.extensions.show
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        RestClient.getClient.getVideoPageData(token!!).enqueue(object :
            Callback<VideoPageResponse> {
            override fun onResponse(
                call: Call<VideoPageResponse>,
                response: Response<VideoPageResponse>
            ) {
                hideProgress()
                val res = response.body()
                hideProgress()
                if (response.code() == 500) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show()
                } else {
                    if (res!!.status!!) {

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
                }
            }

            override fun onFailure(call: Call<VideoPageResponse>, t: Throwable) {

            }

        })
    }
    /*callApi(
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
        })*/


}