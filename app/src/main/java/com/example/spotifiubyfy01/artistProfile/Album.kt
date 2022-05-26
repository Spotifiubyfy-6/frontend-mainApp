package com.example.spotifiubyfy01.artistProfile

import com.example.spotifiubyfy01.search.ALBUM_SEARCH_ITEM
import com.example.spotifiubyfy01.search.SearchItem
import java.io.Serializable

data class Album (
    var album_name: String,
    var album_image: String,
    var artist_name: String,
    var song_list: List<Song>
) : Serializable, SearchItem() {

    override fun getSearchItemType(): Int {
        return ALBUM_SEARCH_ITEM
    }
}