package com.example.pushnotificationexample

import android.app.Application
import com.example.pushnotificationexample.notificationhelper.NotificationHelper

class PushApp : Application() {

    lateinit var notificationHelper: NotificationHelper

    companion object {
        lateinit var instance: PushApp
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        notificationHelper = NotificationHelper(this)
    }
}