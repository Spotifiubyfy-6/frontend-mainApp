package com.example.spotifiubyfy01.artistProfile

import com.example.spotifiubyfy01.search.SearchItem
import com.example.spotifiubyfy01.search.SearchItemEnum
import java.io.Serializable

data class Album (
    var album_id: String,
    var album_name: String,
    var album_image: String,
    var artist_name: String,
    var song_list: List<Song>,
    var album_description: String,
    var album_genre: String,
    var author_id: String
) : Serializable, SearchItem() {

    override fun getSearchItemType(): SearchItemEnum {
        return SearchItemEnum.ALBUM_SEARCH_ITEM
    }
}