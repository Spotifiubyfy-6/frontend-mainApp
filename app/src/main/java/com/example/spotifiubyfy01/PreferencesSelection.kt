package com.example.spotifiubyfy01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class PreferencesSelection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_selection)

        // Get Intent
        val location = intent.getStringExtra("Location")

        // Set title with location
        val textView = findViewById<TextView>(R.id.textView).apply {
            text = "Welcome from " + location.toString() + "\n"  + text
        }
    }
}