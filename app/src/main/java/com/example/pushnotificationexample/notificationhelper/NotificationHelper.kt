package com.example.pushnotificationexample.notificationhelper

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pushnotificationexample.BuildConfig
import com.example.pushnotificationexample.PushApp
import com.example.pushnotificationexample.R
import com.example.pushnotificationexample.broadcast_receiver.Alarm


class NotificationHelper(ctx: Context) : ContextWrapper(ctx) {

    private var manager: NotificationManagerCompat? = null
    private val smallIcon: Int get() = android.R.drawable.stat_notify_chat
    fun initPrimaryChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan1 = NotificationChannel(PRIMARY_CHANNEL,
                    getString(R.string.noti_channel_default), NotificationManager.IMPORTANCE_HIGH)
            chan1.lightColor = Color.GREEN
            chan1.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            getManager()!!.createNotificationChannel(chan1)
        }
    }

    fun getNotification(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
    }

    fun getNotification(title: String, body: String, pendingIntent: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
    }


    fun notify(notification: NotificationCompat.Builder) {
        val preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val notiId = preferences.getInt("last_notification_id", 0)
        getManager()?.let { notificationManager ->
            notificationManager.notify(notiId, notification.build())
            preferences.edit().putInt("last_notification_id", notiId + 1).apply()
        }
    }


    private fun getManager(): NotificationManagerCompat? {
        if (manager == null) {
            manager = NotificationManagerCompat.from(this)
        }
        return manager
    }

    companion object {
        const val PRIMARY_CHANNEL = "default"
    }


    fun setAlarm() {
        val am = PushApp.instance.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val i = Intent(PushApp.instance.applicationContext, Alarm::class.java)
        i.action = "${BuildConfig.APPLICATION_ID}.CHECK_NOTIFICATIONS"

        val receiver = PendingIntent.getBroadcast(PushApp.instance.applicationContext, 1001, i, PendingIntent.FLAG_UPDATE_CURRENT)

        val h = 1
        val m = 1
        val s = 30
        val alarmDelay = (1000 * s * m * h).toLong()
        val alarmTimeAtUTC = System.currentTimeMillis() + alarmDelay

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, receiver)
        } else {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmDelay, receiver)
        }
    }

    fun cancelAlarm() {
        val am = PushApp.instance.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val i = Intent(PushApp.instance.applicationContext, Alarm::class.java)
        i.action = "${BuildConfig.APPLICATION_ID}.CHECK_NOTIFICATIONS"

        val receiver = PendingIntent.getBroadcast(PushApp.instance.applicationContext, 1001, i, PendingIntent.FLAG_UPDATE_CURRENT)

        am.cancel(receiver)
    }
}