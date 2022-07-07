package com.example.spotifiubyfy01.artistProfile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.artistProfile.Album
import kotlin.reflect.KFunction2

class AlbumRecyclerAdapter(
    val albumList: MutableList<Album>,
    private val onClickListener: (Album) -> Unit,
    private val onDeleteButtonListener: KFunction2<Album, Int, Unit>?
): RecyclerView.Adapter<AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_album_profile_item, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = albumList[position]
        holder.render(item, position, onClickListener, onDeleteButtonListener)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    fun deleteItemOfPosition(position: Int) {
        albumList.removeAt(position)
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, albumList.size);
    }
}