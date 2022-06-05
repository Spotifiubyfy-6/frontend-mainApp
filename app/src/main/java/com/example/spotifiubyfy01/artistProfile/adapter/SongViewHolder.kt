package com.example.spotifiubyfy01.artistProfile.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.artistProfile.Song

class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val songName: TextView = view.findViewById(R.id.song_name)
    private val artistName: TextView = view.findViewById(R.id.artist_name)
    private val addToPlaylist : Button = view.findViewById(R.id.button)

    fun render(song: Song, onClickListener: (Song) -> Unit) {
        songName.text = song.song_name
        artistName.text = song.artist
        addToPlaylist.text = "+"

        itemView.setOnClickListener { onClickListener(song) }
    }
}