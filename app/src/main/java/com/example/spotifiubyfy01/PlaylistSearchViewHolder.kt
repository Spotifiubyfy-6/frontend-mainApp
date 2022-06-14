package com.example.spotifiubyfy01.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.artistProfile.Playlist
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchItem

var default_playlist_image = "https://ladydanville.files.wordpress.com/2012/03/blankart.png"

class PlaylistSearchViewHolder(view: View) : SearchViewHolder(view) {

    private val playlistName: TextView = view.findViewById(R.id.playlistName)
    private val userName: TextView = view.findViewById(R.id.playlist_specification)
    private val playlistImage: ImageView = view.findViewById(R.id.playlist_image)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)

    override fun render(item: SearchItem, onClickListener: (SearchItem) -> Unit) {
        val playlist = item as Playlist
        playlistName.text = playlist.playlist_name
        val text = "Playlist by " + playlist.user_name
        userName.text = text
        val coverRef = app.getStorageReference().child(playlist.playlist_image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(playlistImage.context).load(url).into(playlistImage)
        }.addOnFailureListener {
            Glide.with(playlistImage.context).load(default_playlist_image).into(playlistImage)
        }
        itemView.setOnClickListener { onClickListener(playlist) }
    }
}