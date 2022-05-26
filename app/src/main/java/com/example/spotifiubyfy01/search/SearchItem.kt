package com.example.spotifiubyfy01.search

const val ARTIST_SEARCH_ITEM = 0
const val ALBUM_SEARCH_ITEM = 1

abstract class SearchItem {
    abstract fun getSearchItemType(): Int
}
