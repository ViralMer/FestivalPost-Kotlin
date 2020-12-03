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
import com.app.festivalpost.LoginActivity
import com.app.festivalpost.MyBounceInterpolatorNew
import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    var videoView: VideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        imageView = findViewById(R.id.splash)
        //videoView=findViewById(R.id.splash1);

        //UpdateHelper.with(this).onUpdateCheck(this).check();


        /*Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(video);
        videoView.start();*/try {
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
        animateButton()
    }

    fun animateButton() {
        val myAnim = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.bounce_new)
        val interpolator = MyBounceInterpolatorNew(0.1, 20.0)
        myAnim.interpolator = interpolator
        imageView!!.startAnimation(myAnim)
    }

    internal inner class HomeActivityAsync : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            // TODO Auto-generated method stub
            super.onPostExecute(result)
            val loggedIn = Global.getPreference(Constant.PREF_LOGIN, false)
            if (!loggedIn) {
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