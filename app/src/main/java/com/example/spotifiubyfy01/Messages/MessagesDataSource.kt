package com.example.spotifiubyfy01.Messages

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.PopUpWindow
import com.example.spotifiubyfy01.artistProfile.AlbumDataSource
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class ChatList(
    val numberOfArtist: Int,
    val callBack: VolleyCallBack<ChatBundle>
) {
    var artistInserted = 0
    val chatList = ArrayList<ChatBundle>(numberOfArtist)

    fun addArtistWithIdToPositionInList(artist: Artist, position: Int, seen: Boolean) {
        synchronized(this) {
            chatList[position] = ChatBundle(artist, seen)
            artistInserted++
            if (artistInserted == numberOfArtist)
                callBack.updateData(chatList)
        }
    }
}

class MessagesDataSource {

    companion object {

        private fun searchRespectiveArtists(context: Context, idNSeenTuples: JSONArray,
                                            callBack: VolleyCallBack<ChatBundle>) {
            val numberOfArtists = idNSeenTuples.length()
            val chatList = ChatList(numberOfArtists, callBack)
            for (i in 0 until numberOfArtists) {
                val idNSeenTuple = JSONObject(idNSeenTuples.get(i).toString())
                this.searchArist(context, chatList, idNSeenTuple, i)
            }
        }

        private fun searchArist(context: Context, chatList: ChatList, idNSeenTuple: JSONObject,
                                position: Int) {
            val artistId = idNSeenTuple.toString()
            val url = "https://spotifiubyfy-users.herokuapp.com/users/user_by_id/" + artistId
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                Response.Listener { response ->
                    val jsonArtist = JSONObject(response.get(0).toString())
                    val username = jsonArtist.getString("username")
                    val id = jsonArtist.getString("id").toInt()
                    chatList.addArtistWithIdToPositionInList(Artist(id, username, image_link),
                                                            position, false)
                },
                { error -> val intent = Intent(context, PopUpWindow::class.java).apply {
//                    val error = errorResponse//.networkResponse.data.decodeToString() //.split('"')[3]
                    putExtra("popuptext", error.toString())
                }
                    context.startActivity(intent)})
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        fun getChatsOfArtistWithID(context: Context, artist_id: Int,
                                   callBack: VolleyCallBack<ChatBundle>) {
            val url = "https://spotifiubyfy-messages.herokuapp.com/messages/all_conversations/" +
                    artist_id + "?skip=0&limit=100"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                Response.Listener { response ->
                    this.searchRespectiveArtists(context, response, callBack)
                },
                { error -> val intent = Intent(context, PopUpWindow::class.java).apply {
//                    val error = errorResponse//.networkResponse.data.decodeToString() //.split('"')[3]
                    putExtra("popuptext", error.toString())
                }
                    context.startActivity(intent)})
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
            /*chatList.add(ChatBundle(Artist(1, "The Beatles", image_link), false))
            chatList.add(ChatBundle(Artist(1, "The Rollings", image_link), true))
            chatList.add(ChatBundle(Artist(1, "Adele", image_link), true))
            chatList.add(ChatBundle(Artist(1, "Perrito", image_link), true))
            chatList.add(ChatBundle(Artist(1, "El Bicho", image_link), false))*/
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