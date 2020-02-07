package com.example.pushnotificationexample.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pushnotificationexample.R
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        val message = intent.extras?.getString("message")
        val senderName = intent.extras?.getString("sender_name")
        tv_notification.text = String.format("Message from %s\n%s", senderName, message)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isTaskRoot) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            finish()
        }
    }
}
