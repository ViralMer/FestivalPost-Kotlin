package com.app.festivalpost.utility

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.Window
import android.widget.Button
import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity

class NetworkStateChecker : BroadcastReceiver() {
    //context and database helper object
    private var context: Context? = null
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        this.context = context
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo


        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI || activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
            }
        } else {
            val dialog = Dialog(
                context,
                R.style.DialogAnimation
            )
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            dialog.setContentView(R.layout.view_no_internet)
            dialog.setCancelable(true)
            val button = dialog.findViewById<Button>(R.id.btnTryAgain)
            button.setOnClickListener {
                context.startActivity(Intent(context, HomeActivity::class.java))
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}