package com.app.festivalpost

import android.app.Dialog
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.emegamart.lelys.network.RestApis
import com.emegamart.lelys.utils.SharedPrefUtils
import com.google.firebase.FirebaseApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import okhttp3.OkHttpClient


class FestivalPost : MultiDexApplication() {

    private var progressDialog: Dialog? = null

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        FirebaseApp.initializeApp(this)

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
        var restApis: RestApis? = null
        var okHttpClient: OkHttpClient? = null
        var sharedPrefUtils: SharedPrefUtils? = null
        var noInternetDialog: Dialog? = null
        lateinit var language: String
        var appTheme: Int = 0

        fun getAppInstance(): FestivalPost {
            return appInstance
        }


    }
}
