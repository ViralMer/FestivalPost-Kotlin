package com.app.festivalpost.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationDismissedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.extras!!.getInt(context.packageName)
        /* Your code to handle the event here */Log.d("Notification", "Cancelled")
    }
}