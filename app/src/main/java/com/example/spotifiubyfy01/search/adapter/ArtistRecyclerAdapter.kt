package com.example.spotifiubyfy01.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist



class ArtistRecyclerAdapter(
    private var artistList: List<Artist>,
    private val onClickListener:(Artist) -> Unit
): RecyclerView.Adapter<ArtistViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        val itemType = artistList[position].javaClass
        if (itemType == Artist::javaClass)
            return 0
        else
            return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val layout =
            when (viewType) {
                0 -> R.layout.layout_artist_list_item
                else -> {1}
            }
        val view =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val item = artistList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return artistList.size
    }

    fun updateList(artistList: List<Artist>) {
        this.artistList = artistList
        notifyDataSetChanged()
    }
}
