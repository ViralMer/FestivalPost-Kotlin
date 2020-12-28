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
    @POST("getLanguageVideo")
    fun getVideoLanguageData(@Field("videoid")videoid:String,@Field("languageid")languageid:String,@Field("token")token:String= getApiToken()):Call<VideoLanguageItemResponse>



    @FormUrlEncoded
    @POST("getfestivalimages")
    fun getCategoryImages(@Field("postcategoryid")postcategoryid:String):Call<CategoryImagesResponse>

    @FormUrlEncoded
    @POST("getmyprofile")
    fun getProfile(@Field("token")token:String= getApiToken()):Call<ProfileResponse>

    @FormUrlEncoded
    @POST("editmyprofile")
    fun editmyprofile(@Field("name")name:String, @Field("email")email:String,@Field("token")token:String= getApiToken()):Call<ProfileResponse>


    @FormUrlEncoded
    @POST("getCustomCategoryPosts")
    fun getCustomCategoryPosts(@Field("token")token:String= getApiToken()):Call<CustomCategoryResponse>


    @FormUrlEncoded
    @POST("getLanguageCustomeCategoryPost")
    fun getLanguageCustomeCategoryPost(@Field("catid")catid:String,@Field("token")token:String= getApiToken(),@Field("languageid")languageid:String= "0"):Call<CustomCategoryPostResponse>

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


    @FormUrlEncoded
    @POST("markascurrentbusiness")
    fun markascurrentbusiness(@Field("business_id")business_id:String= getApiToken(),@Field("token")token:String= getApiToken()):Call<BusinessItemResponse>

    @FormUrlEncoded
    @POST("getdays")
    fun getdays(@Field("date")date:String,@Field("token")token:String= getApiToken()):Call<DaysPageResponse>

    @FormUrlEncoded
    @POST("GetAllFestivalVideo")
    fun getAllFestivalVideo(@Field("date")date:String,@Field("token")token:String= getApiToken()):Call<VideoItemResponse>



}
