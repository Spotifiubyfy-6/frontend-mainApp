package com.example.spotifiubyfy01.Messages

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.MyRequestQueue
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.reflect.KFunction1

class MessagesDataSender {

    companion object {
        fun sendMessage(
            context: Context,
            senderId: Int,
            receiverId: Int,
            message: String,
            addMessage: (Message, LocalDateTime) -> Unit
        ) {
            if (message.isEmpty())
                return
            val url = "http://spotifiubyfy-messages.herokuapp.com/messages/send"
            val jsonRequest: StringRequest = object : StringRequest(
                Method.POST, url, { response ->
                    val jsonMessage = JSONObject(response)
                    val dateNTime =
                        MessagesDataSource.obtainDate(jsonMessage.get("time") as String)
                    val messageItem = MessagesDataSource.getMessage(senderId, jsonMessage, dateNTime)
                    addMessage(messageItem, dateNTime) },
                { errorResponse ->
                    Log.d("TAG", errorResponse.toString())
                }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    val params2 = HashMap<String, String>()
                    params2["sender_id"] = senderId.toString()
                    params2["receiver_id"] = receiverId.toString()
                    params2["message"] = message
                    return JSONObject(params2 as Map<String, String>).toString().toByteArray()
                }
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(jsonRequest)
        }
    }
}