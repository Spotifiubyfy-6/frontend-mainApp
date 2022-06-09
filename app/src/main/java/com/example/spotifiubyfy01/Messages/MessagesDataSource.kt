package com.example.spotifiubyfy01.Messages

import android.content.Context
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack

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
            messagesList.add(Message(requesterId, requesterId, otherId, "hello!!", null))
            messagesList.add(Message(requesterId, otherId, requesterId, "whats up?!", null))
            messagesList.add(Message(requesterId, requesterId, otherId, "how are you?", null))
            messagesList.add(Message(requesterId, otherId, requesterId,"good wbu?", null))
            messagesList.add(Message(requesterId, requesterId, otherId, "everything blessed!", null))

            callBack.updateData(messagesList)
        }
    }
}