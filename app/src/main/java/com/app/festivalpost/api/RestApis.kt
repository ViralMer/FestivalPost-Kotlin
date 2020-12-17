package com.emegamart.lelys.network

import com.app.festivalpost.models.*
import com.emegamart.lelys.models.BaseResponse
import com.emegamart.lelys.utils.extensions.*
import retrofit2.Call
import retrofit2.http.*

interface RestApis {


    @FormUrlEncoded
    @POST("gethomepage")
    fun getHomePageData(@Field("token")token:String= getApiToken()):Call<HomePageResponse>


    @FormUrlEncoded
    @POST("getAllVideoPosts")
    fun getVideoPageData(@Field("token")token:String= getApiToken()):Call<VideoPageResponse>

    @FormUrlEncoded
    @POST("getfestivalimages")
    fun getCategoryImages(@Field("postcategoryid")postcategoryid:String):Call<CategoryImagesResponse>

    @FormUrlEncoded
    @POST("getmyprofile")
    fun getProfile(@Field("token")token:String= getApiToken()):Call<ProfileResponse>

    @FormUrlEncoded
    @POST("addbusiness")
    fun addBusiness(@Field("token")token:String= getApiToken()):Call<AddBussiensResponse>

    @FormUrlEncoded
    @POST("updatebusiness")
    fun updateBusiness(@Field("token")token:String= getApiToken()):Call<UpdateBussiensResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name")token:String, @Field("mobile")mobile:String, @Field("device_id")device_id:String= getDeviceID(), @Field("device")device:String= getDeviceType(),@Field("device_token")device_token:String= getDeviceToken()):Call<RegisterResponse>


    @FormUrlEncoded
    @POST("login")
    fun login(@Field("mobile")mobile:String, @Field("device_id")device_id:String= getDeviceID(), @Field("device")device:String= getDeviceType(),@Field("device_token")device_token:String= getDeviceToken()):Call<LoginResponse>

    @FormUrlEncoded
    @POST("getmyallbusiness")
    fun getAllMyBusiness(@Field("token")token:String= getApiToken()):Call<BusinessItemResponse>


    @FormUrlEncoded
    @POST("getBusinessCategory")
    fun getAllBusinessCategory(@Field("token")token:String= getApiToken()):Call<BusinessCategoryResponse>



}
