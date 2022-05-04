package com.example.spotifiubyfy01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainLandingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_landing_page)

        val signUpClick = findViewById<Button>(R.id.sing_up_button)
        signUpClick.setOnClickListener {
            val intent = Intent(this, SignInLandingPage::class.java)
            startActivity(intent)
        }

        val logInClick = findViewById<Button>(R.id.log_in_button)
        logInClick.setOnClickListener {
            val intent = Intent(this, LogInPage::class.java)
            startActivity(intent)
        }

    }
}