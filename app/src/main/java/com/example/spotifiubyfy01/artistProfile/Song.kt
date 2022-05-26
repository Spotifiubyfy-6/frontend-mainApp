package com.example.spotifiubyfy01.artistProfile

import java.io.Serializable

data class Song (
    var song_name: String,
    var album_id: Int,
    var id: Int,
    var storage_name: String
) : Serializable
