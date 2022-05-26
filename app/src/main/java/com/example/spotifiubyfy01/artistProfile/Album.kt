package com.example.spotifiubyfy01.artistProfile

import java.io.Serializable

data class Album (
    var album_name: String,
    var album_image: String,
    var song_list: List<Song>
) : Serializable