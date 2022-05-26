package com.example.spotifiubyfy01

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.artistProfile.adapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.artistProfile.adapter.SongRecyclerAdapter
import com.example.spotifiubyfy01.search.VolleyCallBack

class AlbumPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_page)
        val album = intent.extras?.get("Album") as Album
        findViewById<TextView>(R.id.albumName).text = album.album_name
        findViewById<TextView>(R.id.artistName).text = album.artist_name
        val image = findViewById<ImageView>(R.id.album_image)
        Glide.with(image.context).load(album!!.album_image).into(image)
        initRecyclerView(album.song_list)
    }

    private fun initRecyclerView(songList: List<Song>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SongRecyclerAdapter(songList) {song ->
                onItemClicked(song)
            }
    }

    private fun onItemClicked(song: Song) {
        //Do something with the Song
        Log.d(TAG, song.song_name +" with id " + song.id.toString() + " made by " + song.artist)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}