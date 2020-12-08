package com.emegamart.lelys.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings.Secure
import com.app.festivalpost.R
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



}
