package com.example.pushnotificationexample.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.pushnotificationexample.notificationhelper.NotificationHelper

class Alarm : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("Alarm", "$action")
        val notificationHelper = NotificationHelper(context)
        notificationHelper.setAlarm()
    }
}