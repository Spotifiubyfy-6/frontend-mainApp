package com.example.spotifiubyfy01.Messages

import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.PopUpWindow
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class ChatList(
    private val numberOfArtist: Int,
    private val callBack: VolleyCallBack<ChatBundle>
) {
    private var artistInserted = 0
    private val chatList = ArrayList<ChatBundle>()
    init {
        for (i in 0 until numberOfArtist)
            chatList.add(ChatBundle(Artist(0,"kotfu", "cklin"), 0))
    }

    fun addArtistWithIdToPositionInList(artist: Artist, position: Int, numberOfNotSeen: Int) {
        synchronized(this) {
            chatList.set(position, ChatBundle(artist, numberOfNotSeen))
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
                    val artistName = jsonArtist.getString("name")
                    var numberOfNotSeen = 1
                    if (idNSeenTuple.get("seen") as Boolean) {
                        numberOfNotSeen = 0
                    }
                    chatList.addArtistWithIdToPositionInList(Artist(artistId, artistName, image_link),
                                                            position, numberOfNotSeen)
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
                    val jsonArrayMessages = JSONArray(response)
                    if (jsonArrayMessages.length() != 0) {
                        var currentDay = addFirstDateAndMessage(messagesList,
                                            JSONObject(jsonArrayMessages.
                                                        get(jsonArrayMessages.length() -1).toString()),
                                            requesterId)
                        for (i in (0 until jsonArrayMessages.length() - 1).reversed()) {
                            val jsonMessage = JSONObject(jsonArrayMessages.get(i).toString())
                            val dateNTime = obtainDate(jsonMessage.get("time") as String)
                            if ((messagesList.last() as Message).addMessageIfSameTimeNReceiver(
                                    jsonMessage.get("receiver") as Int,
                                    jsonMessage.get("message") as String, dateNTime))
                                    continue
                            val messagedDay = LocalDate.of(dateNTime.year, dateNTime.month, dateNTime.dayOfMonth)
                            if (messagedDay > currentDay) {
                                messagesList.add(DateItem(dateNTime))
                                currentDay = messagedDay
                            }
                            val message = this.getMessage(requesterId, jsonMessage, dateNTime)
                            messagesList.add(message)
                        }
                    }
                    callBack.updateData(messagesList) },
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
        }

        private fun addFirstDateAndMessage(messagesList: ArrayList<MessageItem>,
                                           jsonMessage: JSONObject, requesterId: Int): LocalDate {
            val dateNTime = obtainDate(jsonMessage.get("time") as String)
            messagesList.add(DateItem(dateNTime))
            val message = this.getMessage(requesterId, jsonMessage, dateNTime)
            messagesList.add(message)
            return LocalDate.of(dateNTime.year, dateNTime.month, dateNTime.dayOfMonth)
        }

        fun getMessage(requesterId: Int, jsonMessage: JSONObject,
                               dateNTime: LocalDateTime): Message {
            val receiverId = jsonMessage.get("receiver") as Int
            val message = jsonMessage.get("message") as String
            return Message(requesterId, receiverId, message, dateNTime)
        }

        fun obtainDate(jsonTime: String): LocalDateTime {
            val year = jsonTime.substringBefore("-")
            val month = jsonTime.substringAfter("$year-").substringBefore("-")
            val day = jsonTime.substringAfter("$year-$month-").substringBefore("T")
            val hour = jsonTime.substringAfter("T").substringBefore(":")
            val minute = jsonTime.substringAfter("T$hour:").substringBefore(":")
            return LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())
        }
    }
}
