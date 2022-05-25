package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.lang.Thread.sleep


class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        var app = (this.application as Spotifiubify)

        findViewById<TextView>(R.id.username).text = app.getProfileData("username")
        findViewById<TextView>(R.id.email).text = app.getProfileData("email")
        findViewById<TextView>(R.id.subscription).text = app.getProfileData("user_suscription")

        if ( !intent.getStringExtra("passwordChangeSuccess").isNullOrEmpty()) {
            Toast.makeText(this, intent.getStringExtra("passwordChangeSuccess"),Toast.LENGTH_LONG).show()
        }
        val logOutClick = findViewById<Button>(R.id.logOutButton)
        logOutClick.setOnClickListener {
            val sharedPref = getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(getString(R.string.token_key), "")
                apply()
            }
            val intent = Intent(this, MainLandingPage::class.java)
            startActivity(intent)
        }

        val editClick = findViewById<Button>(R.id.editButton)
        editClick.setOnClickListener {
            val intent = Intent(this, ProfileEditPage::class.java)
            startActivity(intent)
            finish()
        }

        val createAlbumClick = findViewById<Button>(R.id.createAlbumButton)
        createAlbumClick.setOnClickListener {
            val intent = Intent(this, AlbumCreationPage::class.java)
            startActivity(intent)
        }
    }
}