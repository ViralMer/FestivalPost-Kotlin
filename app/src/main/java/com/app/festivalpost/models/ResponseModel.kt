package com.emegamart.lelys.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings.Secure
import com.app.festivalpost.R
import com.app.festivalpost.models.BusinessItem
import java.io.Serializable

@SuppressLint("HardwareIds")
class DeviceInfo(_context: Context) : Serializable {
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
        deviceUDID = Secure.getString(
            _context.contentResolver,
            Secure.ANDROID_ID
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

    data class HomePageResponse(
        val slider:ArrayList<HomePageItem>,
        val festival:ArrayList<HomePageItem>,
        val cateogry:ArrayList<HomePageItem>,
        val current_business:CurrentBusinessItem
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



}
