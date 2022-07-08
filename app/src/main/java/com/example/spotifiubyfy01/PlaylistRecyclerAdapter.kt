package com.example.spotifiubyfy01

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import kotlin.reflect.KFunction2

class PlaylistRecyclerAdapter(
    private val playlistList: MutableList<Playlist>,
    private val onClickListener:(Playlist) -> Unit,
    private val onDeleteButtonListener: KFunction2<Playlist, Int, Unit>?
): RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_album_profile_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = playlistList[position]
        holder.render(item, position, onClickListener, onDeleteButtonListener)
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    fun deleteItemOfPosition(position: Int) {
        playlistList.removeAt(position)
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, playlistList.size);
    }
}