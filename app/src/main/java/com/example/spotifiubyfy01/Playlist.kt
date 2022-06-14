package com.example.spotifiubyfy01.artistProfile

import com.example.spotifiubyfy01.search.SearchItem
import com.example.spotifiubyfy01.search.SearchItemEnum
import java.io.Serializable

data class Playlist (
    var playlist_id: String,
    var playlist_name: String,
    var playlist_image: String,
    var user_name: String,
    var song_list: List<Song>
) : Serializable, SearchItem() {

    override fun getSearchItemType(): SearchItemEnum {
        return SearchItemEnum.PLAYLIST_SEARCH_ITEM
    }
}