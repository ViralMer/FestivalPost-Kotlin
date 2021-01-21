package com.app.festivalpost.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.DeviceInfo1
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    var videoView: VideoView? = null
    var sessionManager: SessionManager? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        imageView = findViewById(R.id.splash)
        FirebaseApp.initializeApp(this)
        sessionManager=SessionManager(this)
        val deviceInfo = DeviceInfo1(this)
        sessionManager!!.setStringValue(Constants.KeyIntent.DEVICE_TYPE,"Android")
        sessionManager!!.setStringValue(Constants.KeyIntent.DEVICE_ID,deviceInfo.deviceUDID)
        mAuth = FirebaseAuth.getInstance();
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            sessionManager!!.setStringValue(Constants.KeyIntent.DEVICE_TOKEN,token)
            Log.d("DEviceToken",""+sessionManager!!.getValueString(Constants.KeyIntent.DEVICE_TOKEN) +" Device id: " +sessionManager!!.getValueString(
                Constants.KeyIntent.DEVICE_ID
            ))
            // send it to server
        }
      try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
        HomeActivityAsync().execute()
        //animateButton()
    }



    internal inner class HomeActivityAsync : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            // TODO Auto-generated method stub
            super.onPostExecute(result)
            if (!sessionManager!!.getBooleanValue(Constants.KeyIntent.IS_PREMIUM)!!) {
                val homeIntent = Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
                startActivity(homeIntent)
                finish()
            } else {
                val homeAct = Intent(
                    this@SplashActivity,
                    HomeActivity::class.java
                )
                startActivity(homeAct)
                finish()
            }
        }
    }
}