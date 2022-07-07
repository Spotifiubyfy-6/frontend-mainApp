package com.example.spotifiubyfy01.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.PlaylistSearchViewHolder
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.SearchItem
import com.example.spotifiubyfy01.search.SearchItemEnum
import com.example.spotifiubyfy01.search.convertIntToSearchEnum

fun createRespectiveHolder(parent: ViewGroup, viewType: Int) : SearchViewHolder{
    val holder =
        when (convertIntToSearchEnum(viewType)) {
            SearchItemEnum.ARTIST_SEARCH_ITEM ->    {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_artist_list_item, parent, false)
                ArtistViewHolder(view)
            }
            SearchItemEnum.ALBUM_SEARCH_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_album_search_item, parent, false)
                AlbumSearchViewHolder(view)
            }
            SearchItemEnum.PLAYLIST_SEARCH_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_playlist_search_item, parent, false)
                PlaylistSearchViewHolder(view)
            }
            SearchItemEnum.SONG_SEARCH_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_song_search_item, parent, false)
                SongSearchViewHolder(view)
            }
        }
    return holder
}

abstract class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun render(item: SearchItem, onClickListener: (SearchItem) -> Unit)
}