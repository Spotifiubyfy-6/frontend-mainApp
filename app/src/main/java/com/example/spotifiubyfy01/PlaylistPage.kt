package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.artistProfile.adapter.SongRecyclerAdapter
import com.example.spotifiubyfy01.artistProfile.adapter.default_album_image
import com.example.spotifiubyfy01.search.SearchArtistPage

class PlaylistPage : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_page)

        val app = (this.application as Spotifiubify)
        val playlist = intent.extras?.get("Playlist") as Playlist
        findViewById<TextView>(R.id.playlistName).text = playlist.playlist_name
        findViewById<TextView>(R.id.userName).text = playlist.user_name
        val image = findViewById<ImageView>(R.id.playlist_image)
        val owned = intent.extras?.get("owned") as Boolean?
        if (owned != null) {
            val editButton = findViewById<Button>(R.id.editPlaylist)
            editButton.visibility = VISIBLE
            editButton.setOnClickListener {
                val intent = Intent(this, PlaylistCreationPage::class.java)
                intent.putExtra("id", playlist.playlist_id)
                intent.putExtra("image", playlist.playlist_image)
                intent.putExtra("name", playlist.playlist_name)
                startActivity(intent)
            }
        }
        val coverRef = app.getStorageReference().child(playlist.playlist_image)

        val inviteBtn = findViewById<Button>(R.id.invite)
        inviteBtn.setOnClickListener {
            val intent = Intent(this, SearchArtistPage::class.java).apply {
                putExtra("invite", true)
                putExtra("playlist_id", playlist.playlist_id)
            }
            startActivity(intent)
        }
        // Solo puede invitar el owner (falta que playlist devuelva username
        // del back, por ahora es deafult_username)
        if (app.getProfileData("username") != playlist.user_name) {
            inviteBtn.visibility = View.INVISIBLE
        }

        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(default_album_image).into(image)
        }
        initRecyclerView(playlist.song_list)
        val playButton = findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener {
            //play album! obtain album songs using album.song_list
            val app = (this.application as Spotifiubify)
            app.songManager.playSongList(playlist.song_list)
            for (song in playlist.song_list)
                Log.d(ContentValues.TAG, song.song_name)
        }
    }

    private fun initRecyclerView(songList: List<Song>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SongRecyclerAdapter(songList, R.layout.layout_song_list_album_item, { song ->
            onItemClicked(song)
        }, null)
    }

    private fun onItemClicked(song: Song) {
        //Do something with the Song
        val app = (this.application as Spotifiubify)
        app.songManager.play(song)
        Log.d(ContentValues.TAG, song.song_name +" with id " + song.id.toString() + " made by " + song.artist)
    }

}