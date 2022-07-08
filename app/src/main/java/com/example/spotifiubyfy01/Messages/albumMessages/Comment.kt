package com.example.spotifiubyfy01.Messages.albumMessages

import com.example.spotifiubyfy01.search.Artist
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

data class Comment(
    var artist: Artist,
    val id: String,
    val comment: String,
    val time: ZonedDateTime,
    val isAuthor: Boolean,
    val ownAlbum: Boolean
) : Serializable {

    fun getTimeAgo(): String {
        val dif = System.currentTimeMillis() - time.withZoneSameInstant(UTC).toInstant().toEpochMilli()
        return toDuration(dif)
    }
}