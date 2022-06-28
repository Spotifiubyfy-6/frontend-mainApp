package com.example.spotifiubyfy01.Messages.albumMessages

import android.text.format.DateUtils.getRelativeTimeSpanString
import java.io.Serializable
import com.example.spotifiubyfy01.search.Artist
import java.time.*

data class Comment(
    var artist: Artist,
    val comment: String,
    val time: LocalDateTime
) : Serializable {

    fun getTimeAgo(): String {
        val dif = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return toDuration(dif)
    }
}