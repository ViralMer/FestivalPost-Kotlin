package com.app.festivalpost

import android.app.Dialog
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.emegamart.lelys.utils.SharedPrefUtils
import com.google.firebase.FirebaseApp
import okhttp3.OkHttpClient


class FestivalPost : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        FirebaseApp.initializeApp(this)


    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private lateinit var appInstance: FestivalPost
        //var restApis: RestApis? = null
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
