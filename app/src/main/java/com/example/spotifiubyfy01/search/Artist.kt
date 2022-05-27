package com.example.spotifiubyfy01.search
import java.io.Serializable

data class Artist(
    var id: Int,
    var username: String,
    var image: String
) : Serializable, SearchItem() {

    override fun getSearchItemType(): SearchItemEnum {
        return SearchItemEnum.ARTIST_SEARCH_ITEM
    }
}
