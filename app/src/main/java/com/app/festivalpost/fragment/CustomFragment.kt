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
import com.app.festivalpost.models.FrameContentItem
import com.app.festivalpost.models.FrameContentListItem
import com.app.festivalpost.models.SliderItem
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class CustomFragment : Fragment(), ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var logout_Status = false
    var message = ""
    var customFrame: RecyclerView? = null
    var viewPager: ViewPager? = null
    var sliderItemArrayList: ArrayList<SliderItem> = ArrayList<SliderItem>()
    var frameContentItemArrayList: ArrayList<FrameContentListItem> =
        ArrayList<FrameContentListItem>()
    var pagerAdapter: PagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        apiManager = ApiManager(activity!!, this@CustomFragment)
        val view = inflater.inflate(R.layout.fragment_custom, container, false)
        viewPager = view.findViewById(R.id.homeSlider)
        customFrame = view.findViewById(R.id.newrecylerview)
        customFrame!!.setLayoutManager(LinearLayoutManager(activity))
        pagerAdapter = PagerAdapter()
        loadDetails()


        /*mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDetails();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });*/
        return view
    }

    private fun loadDetails() {
        Global.showProgressDialog(activity)
        apiManager!!.getcustomcategorypost(ApiEndpoints.getcustomcategorypost)
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

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(activity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(activity!!)
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
            if (requestService.equals(ApiEndpoints.getcustomcategorypost, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        val customFrameAdapter =
                            CustomFrameAdapter(activity!!, frameContentItemArrayList)
                        Log.d("frameListSize", "" + frameContentItemArrayList.size)
                        customFrame!!.adapter = customFrameAdapter
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun processResponse(responseString: String?) {
        status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            Log.d("status", "" + status)
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }
            frameContentItemArrayList.clear()
            if (jsonObject.has("data")) {
                val jsonArray = jsonObject.getJSONArray("data")
                Log.d("dataSize", "" + jsonArray.length())
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f: FrameContentListItem =
                            Gson().fromJson(j.toString(), FrameContentListItem::class.java)
                        val frameContentItemArrayList1: ArrayList<FrameContentItem> =
                            ArrayList<FrameContentItem>()
                        val jsonArraycatdata = j.getJSONArray("catdata")
                        if (jsonArraycatdata.length() > 0) {
                            for (k in 0 until jsonArraycatdata.length()) {
                                val j1 = jsonArraycatdata.getJSONObject(k)
                                val frameContentItem = FrameContentItem()
                                frameContentItem.banner_image=j1.getString("banner_image")
                                frameContentItem.custom_cateogry_id=j1.getString("custom_cateogry_id")
                                val images: FrameContentItem.Images = FrameContentItem.Images()
                                val j2 = j1.getJSONObject("images")
                                images.image_one=j2.getString("image_one")
                                images.img_height=j2.getString("position_x")
                                images.img_width=j2.getString("position_y")
                                images.img_position_x=j2.getString("img_position_x")
                                images.img_position_y=j2.getString("img_position_y")
                                images.position_x=j2.getString("img_height")
                                images.position_y=j2.getString("img_width")
                                frameContentItem.images=(images)
                                frameContentItemArrayList1.add(frameContentItem)
                            }
                        }
                        val frameContentListItem = FrameContentListItem()
                        frameContentListItem.name = j.getString("name")
                        frameContentListItem.custom_cateogry_id = j.getInt("custom_cateogry_id")
                        frameContentListItem.contentItemArrayList = frameContentItemArrayList1
                        frameContentItemArrayList.add(f)
                    }
                }
            }
            if (jsonObject.has("slider")) {
                val jsonArray = jsonObject.getJSONArray("slider")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f: SliderItem = Gson().fromJson(j.toString(), SliderItem::class.java)
                        val sliderItem = SliderItem()
                        sliderItem.custom_cateogry_id = j.getInt("custom_cateogry_id")
                        sliderItem.slider_img = j.getString("slider_img")
                        sliderItem.slider_img_position = j.getInt("slider_img_position")
                        Log.d("SliderImage", "" + j.getString("slider_img"))
                        sliderItemArrayList.add(sliderItem)
                    }
                    Log.d("dataSize1", "" + sliderItemArrayList.size)
                    try {
                        viewPager!!.adapter = pagerAdapter
                        pagerAdapter!!.notifyDataSetChanged()
                    } catch (e: Exception) {
                    }
                }
                Global.dismissProgressDialog(activity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onErrorResponse(requestService: String?, responseString: String?, responseCode: Int) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(activity)
            Global.showFailDialog(activity, responseString)
        }
    }


    inner class PagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        override fun getCount(): Int {
            return sliderItemArrayList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_slider, container, false)
            val festivalItem: SliderItem = sliderItemArrayList[position]
            val ivphoto = view.findViewById<ImageView>(R.id.imgSlider)
            try {
                Glide.with(activity!!).load(festivalItem.slider_img)
                    .placeholder(R.drawable.placeholder_img).error(R.drawable.placeholder_img)
                    .into(ivphoto)
            } catch (e: Exception) {
            }
            //Log.d("image_url",""+festivalItem.getSlider_img());


            /*ivphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        int index = (int) view.getTag();
                        SliderItem f = sliderItemArrayList.get(index);
                        Intent detailact = new Intent(getActivity(), CustomFrameActivity.class);
                        detailact.putExtra("object_slider", f);
                        getActivity().startActivity(detailact);
                }
            });*/container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}