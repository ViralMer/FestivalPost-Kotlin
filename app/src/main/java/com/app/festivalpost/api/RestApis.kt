package com.app.festivalpost.api

import com.app.festivalpost.models.*
import com.emegamart.lelys.models.BaseResponse
import com.emegamart.lelys.utils.extensions.*
import retrofit2.Call
import retrofit2.http.*

interface RestApis {


    @FormUrlEncoded
    @POST("gethomepage")
    fun getHomePageData(@Field("token")token:String):Call<HomePageResponse>


    @FormUrlEncoded
    @POST("getAllVideoPosts")
    fun getVideoPageData(@Field("token")token:String):Call<VideoPageResponse>

    @FormUrlEncoded
    @POST("getLanguageVideo")
    fun getVideoLanguageData(@Field("videoid")videoid:String,@Field("languageid")languageid:String,@Field("token")token:String):Call<VideoLanguageItemResponse>



    @FormUrlEncoded
    @POST("getfestivalimages")
    fun getCategoryImages(@Field("postcategoryid")postcategoryid:String):Call<CategoryImagesResponse>

    @FormUrlEncoded
    @POST("getBusinessCategoryImages")
    fun getBusinessCategoryImages(@Field("token")token:String,@Field("buss_cat_id")buss_cat_id:String):Call<BusinessCategoryItemResponse>

    @FormUrlEncoded
    @POST("getmyprofile")
    fun getProfile(@Field("token")token:String):Call<ProfileResponse>

    @FormUrlEncoded
    @POST("editmyprofile")
    fun editmyprofile(@Field("name")name:String, @Field("email")email:String,@Field("token")token:String):Call<ProfileResponse>


    @FormUrlEncoded
    @POST("getCustomCategoryPosts")
    fun getCustomCategoryPosts(@Field("token")token:String):Call<CustomCategoryResponse>


    @FormUrlEncoded
    @POST("getLanguageCustomeCategoryPost")
    fun getLanguageCustomeCategoryPost(@Field("catid")catid:String,@Field("token")token:String,@Field("languageid")languageid:String= "0"):Call<CustomCategoryPostResponse>

    @FormUrlEncoded
    @POST("addbusiness")
    fun addBusiness(@Field("token")token:String):Call<AddBussiensResponse>

    @FormUrlEncoded
    @POST("updatebusiness")
    fun updateBusiness(@Field("token")token:String):Call<UpdateBussiensResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name")token:String, @Field("mobile")mobile:String, @Field("device_id")device_id:String, @Field("device")device:String,@Field("device_token")device_token:String):Call<RegisterResponse>


    @FormUrlEncoded
    @POST("login")
    fun login(@Field("mobile")mobile:String, @Field("device_id")device_id:String, @Field("device")device:String,@Field("device_token")device_token:String):Call<LoginResponse>

    @FormUrlEncoded
    @POST("getmyallbusiness")
    fun getAllMyBusiness(@Field("token")token:String):Call<BusinessItemResponse>


    @FormUrlEncoded
    @POST("getBusinessCategory")
    fun getAllBusinessCategory(@Field("token")token:String):Call<BusinessCategoryResponse>


    @FormUrlEncoded
    @POST("markascurrentbusiness")
    fun markascurrentbusiness(@Field("business_id")business_id:String,@Field("token")token:String):Call<BusinessItemResponse>

    @FormUrlEncoded
    @POST("getdays")
    fun getdays(@Field("date")date:String,@Field("token")token:String):Call<DaysPageResponse>

    @FormUrlEncoded
    @POST("GetAllFestivalVideo")
    fun getAllFestivalVideo(@Field("date")date:String,@Field("token")token:String):Call<VideoItemResponse>

    @FormUrlEncoded
    @POST("removemybusiness")
    fun removemybusiness(@Field("token")token:String,@Field("id")bussiness_id:String):Call<BaseResponse>

    @FormUrlEncoded
    @POST("purchaseplan")
    fun purchaseplan(@Field("order_id")order_id:String,
                     @Field("purchase_id")payment_id:String,
                     @Field("business_id")business_id:String,
                     @Field("plan_id")plan_id:String,
                     @Field("device")device_id:String,
                     @Field("token")token:String
                     ):Call<PurchasePlanResponse>

    @GET("plans")
    fun plans():Call<PlanListResponse>



}
