package com.example.spotifiubyfy01.search

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R

class ArtistPage: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_page)
        val artist = intent.extras?.get("Artist") as Artist

        val artistName = findViewById<TextView>(R.id.artist_name)
        val image = findViewById<ImageView>(R.id.artist_image)
        artistName.text = artist.username
        Glide.with(image.context).load(artist.image).into(image)
    }
}