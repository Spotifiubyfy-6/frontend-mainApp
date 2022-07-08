package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.Messages.MessagesDataSource
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.time.ZonedDateTime
import kotlin.reflect.KFunction1

class DeleteSender {

    companion object {
        fun deleteAlbum(
            context: Context,
            albumId: String,
            position: Int,
            onDeletion: KFunction1<Int, Unit>
        ) {
            val url = "http://spotifiubyfy-music.herokuapp.com/albums/" + albumId
            val jsonRequest: StringRequest = object : StringRequest(
                Method.DELETE, url, {
                    onDeletion(position)
                                    },
                { errorResponse ->
                    Toast.makeText(context, "An error occured. Please retry again.", Toast.LENGTH_SHORT)
                }){}
            MyRequestQueue.getInstance(context).addToRequestQueue(jsonRequest)
        }

        fun deletePlaylist(
            context: Context,
            playlistId: String,
            position: Int,
            onDeletion: KFunction1<Int, Unit>
        ) {
            val url = "http://spotifiubyfy-music.herokuapp.com/albums/" + playlistId
            val jsonRequest: StringRequest = object : StringRequest(
                Method.DELETE, url, {
                    onDeletion(position)
                },
                { errorResponse ->
                    Toast.makeText(context, "An error occured. Please retry again.", Toast.LENGTH_SHORT)
                }){}
            MyRequestQueue.getInstance(context).addToRequestQueue(jsonRequest)
        }
    }
}