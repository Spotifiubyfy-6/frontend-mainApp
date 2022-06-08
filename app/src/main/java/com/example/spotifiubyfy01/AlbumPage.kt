package com.example.spotifiubyfy01

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.artistProfile.adapter.SongRecyclerAdapter
import com.example.spotifiubyfy01.artistProfile.adapter.default_album_image

class AlbumPage : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val app = (this.application as Spotifiubify)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_page)
        val album = intent.extras?.get("Album") as Album
        findViewById<TextView>(R.id.albumName).text = album.album_name
        findViewById<TextView>(R.id.artistName).text = album.artist_name
        findViewById<TextView>(R.id.album_genre).text = album.album_genre
        findViewById<TextView>(R.id.album_description).text = album.album_description
        val image = findViewById<ImageView>(R.id.album_image)


        val coverRef = app.getStorageReference().child(album.album_image)

        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(default_album_image).into(image)
        }

        initRecyclerView(album.song_list)
        val play_button = findViewById<Button>(R.id.playButton)
        play_button.setOnClickListener {
            //play album! obtain album songs using album.song_list
            val app = (this.application as Spotifiubify)
            app.SongManager.playSongList(album.song_list)
            for (song in album.song_list)
                Log.d(TAG, song.song_name)
        }
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
        val app = (this.application as Spotifiubify)
        app.SongManager.play(song)
        Log.d(TAG, song.song_name +" with id " + song.id.toString() + " made by " + song.artist)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_playback) {
            startActivity(Intent(this, ReproductionPage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}