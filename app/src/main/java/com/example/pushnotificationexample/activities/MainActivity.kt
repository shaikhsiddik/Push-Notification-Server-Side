package com.example.pushnotificationexample.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pushnotificationexample.PushApp
import com.example.pushnotificationexample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PushApp.instance.notificationHelper.initPrimaryChannel()
        PushApp.instance.notificationHelper.cancelAlarm()
    }
}