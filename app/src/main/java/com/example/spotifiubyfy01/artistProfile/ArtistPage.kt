package com.example.spotifiubyfy01.ArtistProfile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.AlbumPage
import com.example.spotifiubyfy01.ArtistProfile.adapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist

class ArtistPage: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_page)
        val artist = intent.extras?.get("Artist") as Artist
        val artistName = findViewById<TextView>(R.id.artist_name)
        val image = findViewById<ImageView>(R.id.artist_image)
        artistName.text = artist.username
        Glide.with(image.context).load(artist.image).into(image)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                                    false)
        recyclerView.adapter =
            AlbumRecyclerAdapter(AlbumDataSource.createAlbumList()) {album ->
                onItemClicked(album)
            }
    }

    private fun onItemClicked(album: Album) {
        val intent = Intent(this, AlbumPage::class.java)
        intent.putExtra("Album", album)
        startActivity(intent)
    }
}