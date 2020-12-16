package com.app.festivalpost.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


data class HomePageResponse(
    val slider:ArrayList<HomePageItem?>,
    val festival:ArrayList<HomePageItem?>,
    val cateogry:ArrayList<HomePageItem?>,
    val frameList:ArrayList<FrameListItem>,
    val current_business: CurrentBusinessItem,
    val current_business_new: CurrentBusinessItem,
    val premium:Boolean?=false,
    val logout:Boolean?=false,
    val current_date:String?=null,
    val share_message:String?=null,
    val status:Boolean?=null,
    val message:String?=null,
    )

    data class HomePageItem(
        val fest_id:String?=null,
        val fest_name:String?=null,
        val fest_info:String?=null,
        val fest_image:String?=null,
        val fest_type:String?=null,
        val fest_date:String?=null,
        val current_date:String?=null,
        val fest_day:String?=null,
        val fest_is_delete:String?=null
    )

    data class CurrentBusinessItem(
        val busi_id:Int?=0,
        val user_id:Int?=0,
        val busi_name:String?=null,
        val busi_email:String?=null,
        val busi_mobile:String?=null,
        val busi_mobile_second:String?=null,
        val busi_website:String?=null,
        val busi_address:String?=null,
        val busi_logo:String?=null,
        val busi_is_approved:Int?=0,
        val busi_delete:Int?=0
    )

data class FrameListItem1(
        val user_frames_id:Int?=0,
        val user_id:Int?=0,
        val business_id:Int?=0,
        val frame_url:String?=null,
        val is_deleted:Int?=0,
        val date_added:String?=null
    )

    data class CategoryImagesResponse(
        val data: ArrayList<CategoryItem?>,
        val status:Boolean?=false,
        val message: String?=null
    )

data class ProfileResponse(
    val data: UserDataItem,
    val status:Boolean?=false,
    val message: String?=null
)

data class AddBussiensResponse(
    val status:Boolean?=false,
    val message: String?=null
)

data class UpdateBussiensResponse(
    val status:Boolean?=false,
    val message: String?=null
)

data class UserDataItem(
    val id:Int?=0,
    val user_credit:Int?=0,
    val default_business_id:Int?=0,
    val name:String?=null,
    val ref_users:String?=null,
    val email:String?=null,
    val mobile:String?=null,
    val email_verified_at:String?=null,
    val status:Int?=0,
    val created_at:String?=null,
    val updated_at:String?=null,
    val ref_code:String?=null,
    val device_id:String?=null,
    val device:String?=null,
    val device_token:String?=null,
)

data class CategoryItem(
    val post_id:Int?=0,
    val post_category_id:Int?=0,
    val post_is_deleted:Int?=0,
    val post_content:String?=null,
    var is_selected:Boolean?=false,

    )

data class RegisterResponse(
    val data: UserDataItem,
    val status:Boolean?=false,
    val message: String?=null,
    val token: String?=null
)

data class LoginResponse(
    val data: UserDataItem,
    val status:Boolean?=false,
    val message: String?=null,
    val token: String?=null
)


