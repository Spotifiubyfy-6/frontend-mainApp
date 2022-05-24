package com.example.spotifiubyfy01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifiubyfy01.search.SearchPage

class MainPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        (this.application as Spotifiubify).setProfile()

        val profile = findViewById<Button>(R.id.profile_button)
        profile.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }

        val search_button = findViewById<Button>(R.id.search_button)
        search_button.setOnClickListener {
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }

    }


}