package com.emegamart.lelys.network

import com.app.festivalpost.models.CategoryImagesResponse
import com.app.festivalpost.models.HomePageResponse
import retrofit2.Call
import retrofit2.http.*

interface RestApis {


    @FormUrlEncoded
    @POST("userapi/v3/gethomepage")
    fun getHomePageData(@Field("token")token:String):Call<HomePageResponse>

    @FormUrlEncoded
    @POST("userapi/v3/getfestivalimages")
    fun getCategoryImages(@Field("postcategoryid")postcategoryid:String):Call<CategoryImagesResponse>



}
