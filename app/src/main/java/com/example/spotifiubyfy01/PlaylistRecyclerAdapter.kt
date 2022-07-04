package com.example.spotifiubyfy01

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Playlist

import com.example.spotifiubyfy01.PlaylistViewHolder

class PlaylistRecyclerAdapter(
    val playlistList: List<Playlist>,
    private val onClickListener:(Playlist) -> Unit
): RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_album_profile_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = playlistList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }
}