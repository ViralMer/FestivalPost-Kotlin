package com.app.festivalpost.activity

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.preference.PreferenceManager
import android.util.Log
import androidx.multidex.MultiDex
import com.app.festivalpost.activity.MyApplication
import com.google.firebase.FirebaseApp

import java.lang.Exception
import kotlin.jvm.Synchronized

class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseApp.initializeApp(this)
        appContext = applicationContext
        init()
        //deviceInfo = DeviceInfo(this)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun init() {
        /* if (!new PermissionModelUtil(this).needPermissionCheck()) {
            getFolderList();
        }*/
        try {
            loadLib()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadLib() {
        /*if (FFmpeg.getInstance(this).isSupported()) {
			Log.d("FFMpEG","Supported");
		} else {
			Log.d("FFMpEG1","Not Supported");
			// ffmpeg is not supported
		}*/
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory()
        Log.d("", "Out of memory")
    }

    companion object {
        @JvmField
		var sharedPref: SharedPreferences? = null
        //var deviceInfo: DeviceInfo? = null
        var appContext: Context? = null
            private set
        val TAG = MyApplication::class.java
            .simpleName

        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }
}