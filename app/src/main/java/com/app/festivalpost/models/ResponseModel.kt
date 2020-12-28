package com.app.festivalpost.models

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import com.app.festivalpost.R
import com.app.festivalpost.utils.Constants.KeyIntent.DEVICE_ID
import com.emegamart.lelys.utils.extensions.getSharedPrefInstance
import java.io.Serializable


data class HomePageResponse(
    val slider: ArrayList<AdvertsieItem?>,
    val festival: ArrayList<HomePageItem?>,
    val cateogry: ArrayList<HomePageItem?>,
    val frameList: ArrayList<FrameListItem1>,
    val current_business: CurrentBusinessItem,
    val current_business_new: CurrentBusinessItem,
    val premium: Boolean? = false,
    val logout: Boolean? = false,
    val current_date: String? = null,
    val share_message: String? = null,
    val status: Boolean? = null,
    val message: String? = null,
)


data class DaysPageResponse(
    val festival: ArrayList<HomePageItem?>,
    val status: Boolean? = null,
    val message: String? = null,
)



data class VideoPageResponse(
    val festival: ArrayList<VideoPageItem?>,
    val category: ArrayList<VideoPageItem?>,
    val status: Boolean? = null,
    val message: String? = null,
)

data class HomePageItem(
    val fest_id: String? = null,
    val fest_name: String? = null,
    val fest_info: String? = null,
    val fest_image: String? = null,
    val fest_type: String? = null,
    val fest_date: String? = null,
    val current_date: String? = null,
    val fest_day: String? = null,
    val fest_is_delete: String? = null
) : Serializable

data class AdvertsieItem(
    val adv_image: String? = null,
    val adv_link: String? = null,
    val adv_number: String? = null,
    val id: String? = null,

) : Serializable

data class VideoPageItem(
    val id: String? = null,
    val video_name: String? = null,
    val video_date: String? = null,
    val video_image: String? = null,
    val video_type: String? = null,
)

data class VideoLanguageItemResponse(
    val data: ArrayList<VideoLanguageItem?>,
    val status: Boolean? = false,
    val message: String? = null
)

data class VideoLanguageItem(
    val id: String? = null,
    val image: String? = null,
    val video: String? = null,
    val type: String? = null,
    var isIs_selected: Boolean = false
) : Serializable


data class VideoItemResponse(
    val video: ArrayList<VideoItem?>,
    val status: Boolean? = false,
    val message: String? = null
)

data class VideoItem(
    val video_id: String? = null,
    val video_name: String? = null,
    val video_image: String? = null,
    val video_date: String? = null,
    ) : Serializable

data class CurrentBusinessItem(
    val busi_id: Int? = 0,
    val user_id: Int? = 0,
    val busi_name: String? = null,
    val busi_email: String? = null,
    val busi_mobile: String? = null,
    val business_category: String? = null,
    val busi_mobile_second: String? = null,
    val busi_website: String? = null,
    val busi_address: String? = null,
    val busi_logo: String? = null,
    val busi_is_approved: Int? = 0,
    val busi_delete: Int? = 0,
    val plan_name: String? = null,
    val purc_start_date: String? = null,
    val purc_end_date: String? = null,
    val need_to_upgrade: String? = null,
    val plan_id: String? = null,
    var is_current_business: Int? = 0
) : Serializable

data class FrameListItem1(
    val user_frames_id: Int? = 0,
    val user_id: Int? = 0,
    val business_id: Int? = 0,
    val frame_url: String? = null,
    val is_deleted: Int? = 0,
    val date_added: String? = null
)

data class CategoryImagesResponse(
    val data: ArrayList<CategoryItem?>,
    val status: Boolean? = false,
    val message: String? = null
)

data class ProfileResponse(
    val data: UserDataItem,
    val status: Boolean? = false,
    val message: String? = null
)

data class FileListItem(
    val path: String? = null
)

data class CustomCategoryResponse(
    val data: ArrayList<CustomCategoryItem>,
    val status: Boolean? = false,
    val message: String? = null
)

data class CustomCategoryPostResponse(
    val data: ArrayList<CustomCategoryPostItem>,
    val status: Boolean? = false,
    val message: String? = null
)

data class CustomCategoryPostItem(
    val id: String? = null,
    val banner_image: String? = null,
    val image: String? = null,
    val position_x: String? = null,
    val position_y: String? = null,
    val img_position_x: String? = null,
    val img_position_y: String? = null,
    val img_height: String? = null,
    val img_width: String? = null,
    val type: String? = null,
    var is_selected: Boolean? = false,
) : Serializable

data class CustomCategoryItem(
    val custom_cateogry_id: String? = null,
    val name: String? = null,
    val custom_image: String? = null,
)

data class AddBussiensResponse(
    val status: Boolean? = false,
    val message: String? = null
)

data class UpdateBussiensResponse(
    val status: Boolean? = false,
    val message: String? = null
)

data class UserDataItem(
    val id: Int? = 0,
    val user_credit: Int? = 0,
    val default_business_id: Int? = 0,
    val name: String? = null,
    val ref_users: String? = null,
    val email: String? = null,
    val mobile: String? = null,
    val email_verified_at: String? = null,
    val status: Int? = 0,
    val created_at: String? = null,
    val updated_at: String? = null,
    val ref_code: String? = null,
    val device_id: String? = null,
    val device: String? = null,
    val device_token: String? = null,
)

data class CategoryItem(
    val post_id: Int? = 0,
    val post_category_id: Int? = 0,
    val post_is_deleted: Int? = 0,
    val post_content: String? = null,
    var is_selected: Boolean? = false,
    var image_type: Int? = null,
    var language_id: Int? = null,

    )

data class RegisterResponse(
    val data: ArrayList<UserDataItem?>,
    val status: Boolean? = false,
    val message: String? = null,
    val token: String? = null
)

data class BusinessItemResponse(
    val data: ArrayList<CurrentBusinessItem?>,
    val status: Boolean? = false,
    val message: String? = null,
    val frameList: ArrayList<FrameListItem1>,
    val current_business: CurrentBusinessItem,

    )

data class LoginResponse(
    val data: ArrayList<UserDataItem?>,
    val status: Boolean? = false,
    val message: String? = null,
    val token: String? = null
)


data class BusinessCategoryResponse(
    val cateogry: ArrayList<BusinessCategory?>,
    val status: Boolean? = false,
    val message: String? = null,
)


data class BusinessCategory(
    val category_name: String? = null,
    val id: String? = null,
    var is_selected: Boolean? = false
)


data class FontTypeList(
    val name: String? = null,
    var is_selected: Boolean? = false
)

data class PlanItemDetails(
    val id: String? = null,
    val name: String? = null,
    val price: String? = null,
    val sku: String? = null,


    )

class DeviceInfo1(_context: Context) : Serializable {
    var deviceUDID = ""
    var deviceToken = ""
    var appName = ""
    var appVersion = ""
    var deviceSystemVersion = ""
    var deviceModel = ""
    var deviceName = ""
    var development = ""
    var PlatformType = ""
    var deviceType = ""

    init {
        deviceUDID = Settings.Secure.getString(
            _context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        try {
            appName = _context
                .getString(
                    _context.packageManager.getPackageInfo(
                        _context.packageName, 0
                    ).applicationInfo.labelRes
                )
        } catch (e: PackageManager.NameNotFoundException) {
            appName = _context.getString(R.string.app_name)
            e.printStackTrace()
        }
        try {
            appVersion = _context.packageManager.getPackageInfo(
                _context.packageName, 0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        deviceSystemVersion = Build.VERSION.RELEASE
        deviceModel = Build.MODEL
        deviceName = Build.MANUFACTURER
        development = "production"
        PlatformType = "Android"


    }
}



