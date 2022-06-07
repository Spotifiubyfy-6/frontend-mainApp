package com.example.spotifiubyfy01

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.io.File

class PlaylistCreationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_creation_page)

        val app = (this.application as Spotifiubify)

        val playlistName = findViewById<EditText>(R.id.playlistName)
        val playlistDescription = findViewById<EditText>(R.id.playlistDescription)
        val playlistMediaPath = findViewById<EditText>(R.id.playlistMedia)

        val createPlaylistButton = findViewById<Button>(R.id.createPlaylistPageButton)

        createPlaylistButton.setOnClickListener {
            val requestBody = JSONObject()

            requestBody.put("playlist_name", playlistName.text.toString())
            requestBody.put("playlist_description", playlistDescription.text.toString())
            requestBody.put("user_id", app.getProfileData("id"))

            val url = "https://spotifiubyfy-music.herokuapp.com/playlists"

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response ->

                    val intent = Intent(this, PlaylistPage::class.java).apply {
                        putExtra("playlist_id", response.getString("id"))
                        putExtra("playlist_name", playlistName.text.toString())
                        val storageName = "covers/"+response.getString("playlist_media")
                        val coverRef =  app.getStorageReference().child(storageName)
                        val file = Uri.fromFile(File(playlistMediaPath.text.toString()))
                        val uploadTask = coverRef.putFile(file)
                        uploadTask.addOnFailureListener {
                            Toast.makeText(app, "Cover not uploaded: ERROR", Toast.LENGTH_LONG).show()
                        }.addOnSuccessListener {
                            Toast.makeText(app, "Cover successfully uploaded", Toast.LENGTH_SHORT).show()
                        }
                    }
                    startActivity(intent)},
                { val intent = Intent(this, PopUpWindow::class.java).apply {
//                    val error = errorResponse//.networkResponse.data.decodeToString() //.split('"')[3]
                    putExtra("popuptext", "cant create playlist right now")
                }
                    startActivity(intent)})

            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
        }
    }
}
