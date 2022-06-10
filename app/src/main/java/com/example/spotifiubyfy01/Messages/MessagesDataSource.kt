package com.example.spotifiubyfy01.Messages

import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.MainPage
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.PopUpWindow
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class ChatList(
    private val numberOfArtist: Int,
    private val callBack: VolleyCallBack<ChatBundle>
) {
    private var artistInserted = 0
    private val chatList = ArrayList<ChatBundle>()
    init {
        chatList.add(ChatBundle(Artist(0,"fu", "kot"), true))
        chatList.add(ChatBundle(Artist(0,"lin", "ck"), true))
    }

    fun addArtistWithIdToPositionInList(artist: Artist, position: Int, seen: Boolean) {
        synchronized(this) {
            chatList.set(position, ChatBundle(artist, seen))
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
                this.searchArtist(context, chatList, idNSeenTuple, i)
            }
        }

        private fun searchArtist(context: Context, chatList: ChatList, idNSeenTuple: JSONObject,
                                 position: Int) {
            val artistId = idNSeenTuple.get("id") as Int
            val url = "https://spotifiubyfy-users.herokuapp.com/users/user_by_id/" + artistId
            val getRequest = JsonObjectRequest(
                Request.Method.GET,
                url, null,
                Response.Listener { jsonArtist ->
                    val username = jsonArtist.getString("username")
                    chatList.addArtistWithIdToPositionInList(Artist(artistId, username, image_link),
                                                            position,
                                                            idNSeenTuple.get("seen") as Boolean)
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
        }

        fun getConversationBetween(context: Context, requesterId: Int, otherId: Int,
                                   callBack: VolleyCallBack<MessageItem>) {
            val messagesList = ArrayList<MessageItem>()
            val url = "http://spotifiubyfy-messages.herokuapp.com/messages/conversation?skip=0&limit=100"
            val jsonRequest: StringRequest = object : StringRequest(
                Method.POST, url, { response ->
                    Log.d("TAG", response.toString())},
                { errorResponse ->
                    Log.d("TAG", errorResponse.toString())
                }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    val params2 = HashMap<String, String>()
                    params2["user_who_request"] = requesterId.toString()
                    params2["another_user"] = otherId.toString()
                    return JSONObject(params2 as Map<String, String>).toString().toByteArray()
                }
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(jsonRequest)
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
            /*val jsonTime = "2022-06-10T13:24:35.769910"
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

            callBack.updateData(messagesList)*/
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