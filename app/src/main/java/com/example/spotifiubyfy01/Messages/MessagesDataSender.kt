package com.example.spotifiubyfy01.Messages

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.PopUpWindow
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.time.LocalDateTime
import java.time.ZonedDateTime

class MessagesDataSender {

    companion object {
        fun sendMessage(
            context: Context,
            senderId: Int,
            receiverId: Int,
            message: String,
            addMessage: (Message, ZonedDateTime) -> Unit
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
                { errorResponse -> val intent = Intent(context, PopUpWindow::class.java).apply {
                    var body = "undefined error"
                    if (errorResponse.networkResponse.data != null) {
                        try {
                            body = String(errorResponse.networkResponse.data, Charsets.UTF_8)
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace()
                        }
                    }
                    putExtra("popuptext", body)
                }
                    ContextCompat.startActivity(context, intent, null)
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