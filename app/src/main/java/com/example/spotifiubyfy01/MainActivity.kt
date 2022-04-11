package com.example.spotifiubyfy01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        var editText = findViewById<EditText>(R.id.registration_username)
        val username = editText.text.toString()
        editText = findViewById(R.id.registration_password)
        val password = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra("new_username", username)
            putExtra("new_password", password)

        }
        startActivity(intent)

    }
}