package com.example.pushnotificationexample.backgroundservice

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.example.pushnotificationexample.PushApp
import com.example.pushnotificationexample.R
import com.example.pushnotificationexample.activities.NotificationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


open class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val mTAG: String = MyFirebaseMessagingService::class.java.simpleName

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(mTAG, "Token:-> $token")
        val preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        preferences.edit().putString("token", token).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(mTAG, "onMessageReceived:-> ${remoteMessage.data}")
        val message = remoteMessage.data["message"]
        val senderName = remoteMessage.data["sender_name"]
        val notificationHelper = PushApp.instance.notificationHelper
        val notificationIntent = Intent(this, NotificationActivity::class.java)
        notificationIntent.putExtra("message", message)
        notificationIntent.putExtra("sender_name", senderName)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivities(this, 1001, arrayOf(notificationIntent), PendingIntent.FLAG_ONE_SHOT)
        val notification = notificationHelper.getNotification("Message from $senderName", message.toString(), pendingIntent)
        notificationHelper.notify(notification)
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn = pm.isInteractive
        if (!isScreenOn) {
            val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getString(R.string.wakelock_tag))
            wl.acquire(3000)
            wl.release()
        }
        notificationHelper.setAlarm()
    }
}