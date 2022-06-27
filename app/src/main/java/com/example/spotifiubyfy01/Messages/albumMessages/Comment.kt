package com.example.spotifiubyfy01.Messages.albumMessages

import java.io.Serializable
import com.example.spotifiubyfy01.search.Artist
import java.time.LocalDateTime

data class Comment(
    val artist: Artist,
    val comment: String,
    val time: LocalDateTime
) : Serializable {
}