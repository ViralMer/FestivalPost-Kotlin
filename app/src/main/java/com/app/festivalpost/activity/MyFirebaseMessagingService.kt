package com.app.festivalpost.activity

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.festivalpost.R
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var mNotificationManager: NotificationManager? = null
    var context: Context? = null
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        Global.storePreference(Constant.PREF_DEVICE_TOKEN, token)
        context = applicationContext
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived Called")
        Log.d(TAG, "From: " + remoteMessage.from)
        val map = remoteMessage.data
        for ((key, value) in map) {
            Log.d("Key", key)
            Log.d("Value", value)
        }
        var message: String? = ""
        if (map.containsKey("message")) {
            message = map["message"]
        }
        sendNotification("Hello")
    }

    private fun sendNotification(message: String) {
        mNotificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var homeIntent: Intent? = null
        homeIntent = Intent(this, SplashActivity::class.java)
        homeIntent.putExtra("type", 0)
        homeIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val contentIntent =
            PendingIntent.getActivity(this, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

//        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.notification);
        val CHANNEL_ID = "my_channel_01" // The id of the channel.
        var mBuilder: NotificationCompat.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(application.resources.getString(R.string.app_name))
                .setAutoCancel(true)
                .setGroupSummary(false)
            val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val name: CharSequence =
                getString(R.string.app_name) // The user-visible name of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            //            mChannel.setSound(sound, attributes);
            mNotificationManager!!.createNotificationChannel(mChannel)
        } else {
            mBuilder =
                NotificationCompat.Builder(applicationContext).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(application.resources.getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setGroupSummary(false)
            //            mBuilder.setSound(sound);
        }
        mBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
        mBuilder.setContentText(message)
        mBuilder.setContentIntent(contentIntent)
        mNotificationManager!!.notify(Random().nextInt(), mBuilder.build())
    }

    private fun createOnDismissedIntent(context: Context, notificationId: Int): PendingIntent {
        val intent = Intent(context, NotificationDismissedReceiver::class.java)
        intent.putExtra(packageName, notificationId)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            notificationId, intent, 0
        )
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        fun foregrounded(): Boolean {
            val appProcessInfo = ActivityManager.RunningAppProcessInfo()
            ActivityManager.getMyMemoryState(appProcessInfo)
            return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
        }
    }
}