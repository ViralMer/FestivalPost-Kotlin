package com.app.festivalpost

import android.app.Dialog
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.app.festivalpost.models.DeviceInfo1
import com.app.festivalpost.utils.Constants.KeyIntent.DEVICE_ID
import com.app.festivalpost.utils.Constants.KeyIntent.DEVICE_TOKEN
import com.app.festivalpost.utils.Constants.KeyIntent.DEVICE_TYPE
import com.emegamart.lelys.utils.SharedPrefUtils
import com.emegamart.lelys.utils.extensions.getSharedPrefInstance
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import okhttp3.OkHttpClient


class FestivalPost : MultiDexApplication() {



    override fun onCreate() {
        super.onCreate()
        appInstance = this
        FirebaseApp.initializeApp(appInstance)
        val deviceInfo = DeviceInfo1(this)

        getSharedPrefInstance().setValue(DEVICE_TYPE, "Android")

        getSharedPrefInstance().setValue(DEVICE_ID, deviceInfo.deviceUDID)



        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(resources!!.getString(R.string.font_regular))
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

    }




    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private lateinit var appInstance: FestivalPost
        var okHttpClient: OkHttpClient? = null
        var sharedPrefUtils: SharedPrefUtils? = null
        var noInternetDialog: Dialog? = null
        lateinit var language: String

        fun getAppInstance(): FestivalPost {
            return appInstance
        }


    }
}
