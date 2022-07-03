package com.example.spotifiubyfy01.Messages.albumMessages

import com.example.spotifiubyfy01.search.Artist
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

data class Comment(
    var artist: Artist,
    val comment: String,
    val time: LocalDateTime,
    val isAuthor: Boolean
) : Serializable {

    fun getTimeAgo(): String {
        val dif = System.currentTimeMillis()- time.atZone(UTC).toInstant().toEpochMilli()
        return toDuration(dif)
    }
}