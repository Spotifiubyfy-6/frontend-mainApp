package com.example.spotifiubyfy01.search.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.SearchItem

fun createRespectiveHolder(parent: ViewGroup, viewType: Int) : SearchViewHolder{
    val holder =
        when (viewType) {
            0 ->    {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_artist_list_item, parent, false)
                ArtistViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_album_search_item, parent, false)
                AlbumSearchViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_artist_list_item, parent, false)
                ArtistViewHolder(view)
            }
        }
    return holder
}

abstract class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun render(item: SearchItem, onClickListener: (SearchItem) -> Unit)
}