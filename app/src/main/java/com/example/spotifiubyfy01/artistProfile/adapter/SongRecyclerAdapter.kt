package com.example.spotifiubyfy01.artistProfile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.artistProfile.Song

class SongRecyclerAdapter(
    private var songList: List<Song>,
    private val onClickListener: (Song) -> Unit,
    private val onDeleteButtonClicked: ((Song, Int) -> Unit?)?
): RecyclerView.Adapter<SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_song_list_album_item,
                parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val item = songList[position]
        holder.render(item, position, onClickListener, onDeleteButtonClicked)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    fun deleteItemOfPosition(position: Int) {
        val mutableSongList = songList as MutableList
        mutableSongList.removeAt(position)
        songList = mutableSongList
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, songList.size)
    }
}