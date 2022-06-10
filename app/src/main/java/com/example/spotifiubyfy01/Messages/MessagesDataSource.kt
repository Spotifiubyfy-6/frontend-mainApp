package com.example.spotifiubyfy01.Messages

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class MessagesDataSource {
    companion object {
        fun getChatsOfArtistWithID(context: Context, artist_id: Int,
                                   callBack: VolleyCallBack<ChatBundle>) {
            val chatList = ArrayList<ChatBundle>()
            chatList.add(ChatBundle(Artist(1, "The Beatles", image_link), false))
            chatList.add(ChatBundle(Artist(1, "The Rollings", image_link), true))
            chatList.add(ChatBundle(Artist(1, "Adele", image_link), true))
            chatList.add(ChatBundle(Artist(1, "Perrito", image_link), true))
            chatList.add(ChatBundle(Artist(1, "El Bicho", image_link), false))
            callBack.updateData(chatList)
        }

        fun getConversationBetween(context: Context, requesterId: Int, otherId: Int,
                                   callBack: VolleyCallBack<MessageItem>) {
            val messagesList = ArrayList<MessageItem>()
            //messagesList.addFront
            //var current_day = 0
            //for (i in (0 until jsonMessages.length()).reversed()) {
            //val time = json
            //val message = json
            //obtain messages_not_seen via first call (maybe)
            //if (time.day >= current_day) {
            //      messagesList.add(time.day)
            //      current_day = time.day
            // }
            //messagesList.add(Message(requesterId, requesterId, otherId, message, time.hour))
            // }
            val jsonTime = "2022-06-10T13:24:35.769910"
            val dateAndTime = obtainDate(jsonTime)
            val date =  dateAndTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toString()
            val time = dateAndTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)).toString()
            Log.d("TAG", time)
            messagesList.add(DateItem(date))
            messagesList.add(Message(requesterId, requesterId, otherId, "hello!!", time))
            messagesList.add(Message(requesterId, otherId, requesterId, "whats up?!", time))
            messagesList.add(Message(requesterId, requesterId, otherId, "how are you?", time))
            messagesList.add(Message(requesterId, otherId, requesterId,"good wbu?", time))
            messagesList.add(Message(requesterId, requesterId, otherId, "everything blessed!", time))

            callBack.updateData(messagesList)
        }

        private fun obtainDate(jsonTime: String): LocalDateTime {
            val year = jsonTime.substringBefore("-")
            val month = jsonTime.substringAfter("$year-").substringBefore("-")
            val day = jsonTime.substringAfter("$year-$month-").substringBefore("T")
            val hour = jsonTime.substringAfter("T").substringBefore(":")
            val minute = jsonTime.substringAfter("T$hour:").substringBefore(":")
            return LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())
        }
    }
}