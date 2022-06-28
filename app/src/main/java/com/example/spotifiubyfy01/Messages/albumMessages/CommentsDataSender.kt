package com.example.spotifiubyfy01.Messages.albumMessages

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.Messages.MessagesDataSource
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.search.Artist
import org.json.JSONObject
import java.time.LocalDateTime

class CommentsDataSender {
    companion object {
        fun makeComment(
            context: Context,
            artist: Artist,
            albumId: Int,
            comment: String,
            addComment: (Comment) -> Unit
        ) {
            if (comment.isEmpty())
                return
            val url = "http://spotifiubyfy-messages.herokuapp.com/comments/comment"
            val jsonRequest: StringRequest = object : StringRequest(
                Method.POST, url, { response ->
                    val jsonComment = JSONObject(response)
                    val dateNTime =
                        MessagesDataSource.obtainDate(jsonComment.get("time") as String)
                    val commentItem = Comment(artist, jsonComment.get("comment") as String, dateNTime)
                    addComment(commentItem) },
                { errorResponse ->
                    Log.d("TAG", errorResponse.toString())
                }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    val params2 = HashMap<String, String>()
                    params2["user_id"] = artist.id.toString()
                    params2["album_id"] = albumId.toString()
                    params2["comment"] = comment
                    return JSONObject(params2 as Map<String, String>).toString().toByteArray()
                }
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(jsonRequest)
        }
    }
}