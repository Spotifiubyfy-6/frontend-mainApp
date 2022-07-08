package com.example.spotifiubyfy01.artistProfile

import com.example.spotifiubyfy01.search.SearchItem
import com.example.spotifiubyfy01.search.SearchItemEnum
import java.io.Serializable

data class Song (
    var song_name: String,
    var artist: String,
    var album_id: Int,
    var id: Int,
    var storage_name: String,
    var album_cover: String,
    val forUsersProfile: Boolean
) : Serializable, SearchItem() {
    override fun getSearchItemType(): SearchItemEnum {
        return SearchItemEnum.SONG_SEARCH_ITEM
    }
}
