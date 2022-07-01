package com.example.spotifiubyfy01.Messages.albumMessages

import android.text.format.DateUtils.getRelativeTimeSpanString
import android.util.Log
import java.io.Serializable
import com.example.spotifiubyfy01.search.Artist
import java.time.*
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