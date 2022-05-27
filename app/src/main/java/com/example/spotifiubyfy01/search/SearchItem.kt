package com.example.spotifiubyfy01.search

import java.lang.RuntimeException

fun convertIntToSearchEnum(value: Int): SearchItemEnum {
    return when(value) {
        0 -> SearchItemEnum.ARTIST_SEARCH_ITEM
        1 -> SearchItemEnum.ALBUM_SEARCH_ITEM
        else -> throw RuntimeException() //should never happen
    }
}

enum class SearchItemEnum {
    ARTIST_SEARCH_ITEM, ALBUM_SEARCH_ITEM; //NEVER CHANGE THIS ORDER
}

abstract class SearchItem {
    abstract fun getSearchItemType(): SearchItemEnum
}
