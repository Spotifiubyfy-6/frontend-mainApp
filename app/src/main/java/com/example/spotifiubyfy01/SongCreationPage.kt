package com.example.spotifiubyfy01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class SongCreationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_creation_page)

        if ( !intent.getStringExtra("songUploaded").isNullOrEmpty()) {
            Toast.makeText(this, intent.getStringExtra("songUploaded"),Toast.LENGTH_LONG).show()
        }

        var app = (this.application as Spotifiubify)

        val songName = findViewById<EditText>(R.id.songName)

        val uploadSongButton = findViewById<Button>(R.id.upload_song_button)

        uploadSongButton.setOnClickListener {
            val requestBody = JSONObject()

            requestBody.put("song_name", songName.text.toString())
            requestBody.put("song_description", "hardcodeado")
            requestBody.put("album_id", intent.getStringExtra("album_id"))

            val url = "http://spotifiubyfy-music.herokuapp.com/music"

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response -> val intent = Intent(this, SongCreationPage::class.java).apply {
                    putExtra("songUploaded", "Song successfully created")
//                  todo: cargar link de imagen a firebase
                }
                    startActivity(intent)},
                { val intent = Intent(this, PopUpWindow::class.java).apply {
//                    val error = errorResponse//.networkResponse.data.decodeToString() //.split('"')[3]
                    putExtra("popuptext", "cant create album right now")
                }
                    startActivity(intent)})

            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
        }
    }
}