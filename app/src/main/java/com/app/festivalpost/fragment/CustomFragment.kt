package com.app.festivalpost.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.festivalpost.R
import com.app.festivalpost.adapter.CustomFrameAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.CustomCategoryItem
import com.app.festivalpost.models.FrameContentItem
import com.app.festivalpost.models.FrameContentListItem
import com.app.festivalpost.models.SliderItem
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.show
import com.emegamart.lelys.utils.extensions.toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_custom.*
import org.json.JSONObject
import java.util.*

class CustomFragment : BaseFragment() {
    var customFrame: RecyclerView? = null
    var viewPager: ViewPager? = null
    var frameContentItemArrayList = arrayListOf<CustomCategoryItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_custom, container, false)
        viewPager = view.findViewById(R.id.homeSlider)
        customFrame = view.findViewById(R.id.rvCustomPost)
        loadDetails()

        return view
    }

    private fun loadDetails() {

        showProgress()
        callApi(

            getRestApis().getCustomCategoryPosts(), onApiSuccess = {
                hideProgress()
                frameContentItemArrayList = it.data
                Log.d("CustomCategorySize",""+frameContentItemArrayList.size)
                if (frameContentItemArrayList.isNotEmpty()) {
                    linearCustomCategorry.show()
                    val customFrameAdapter =
                        CustomFrameAdapter(activity!!, frameContentItemArrayList)
                    customFrame!!.adapter = customFrameAdapter
                } else {
                    toast("No Data Found")
                }


            }, onApiError = {
                hideProgress()

            }, onNetworkError = {
                hideProgress()

            })
    }

}