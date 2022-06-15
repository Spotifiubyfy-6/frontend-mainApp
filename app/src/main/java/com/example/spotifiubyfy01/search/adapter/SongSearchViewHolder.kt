package com.example.spotifiubyfy01.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.search.SearchItem

class SongSearchViewHolder(view: View): SearchViewHolder(view) {

    private val songName: TextView = view.findViewById(R.id.song_name)
    private val artistName: TextView = view.findViewById(R.id.song_specification)
    private val albumImage: ImageView = view.findViewById(R.id.album_image)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)

    override fun render(item: SearchItem, onClickListener: (SearchItem) -> Unit) {
        val song = item as Song
        songName.text = song.song_name
        val text = "Song by " + song.artist
        artistName.text = text
        val coverRef = app.getStorageReference().child(song.album_cover)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(albumImage.context).load(url).into(albumImage)
        }.addOnFailureListener {
            Glide.with(albumImage.context).load(com.example.spotifiubyfy01.artistProfile.adapter.default_album_image).into(albumImage)
        }
        itemView.setOnClickListener { onClickListener(song) }
    }
}