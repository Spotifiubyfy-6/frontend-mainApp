package com.example.spotifiubyfy01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DisplayMessageActivity : NotificationReceiverActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)

        // Get the Intent that started this activity and extract the string
        val username = intent.getStringExtra("new_username")
        val password = intent.getStringExtra("new_password")

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.chosenUsername).apply {
            text = username
        }
        val textView2 = findViewById<TextView>(R.id.chosenPassword).apply {
            text = password
        }
    }

}