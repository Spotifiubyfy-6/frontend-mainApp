package com.example.spotifiubyfy01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class AlbumCreationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_creation_page)
        val app = (this.application as Spotifiubify)

        val albumName = findViewById<EditText>(R.id.albumName)
        val albumDescription = findViewById<EditText>(R.id.albumDescription)
        val albumGenre = findViewById<EditText>(R.id.albumGenre)
        val albumMediaPath = findViewById<EditText>(R.id.albumMediaPath)

        val createAlbumButton = findViewById<Button>(R.id.createAlbumButton)

        createAlbumButton.setOnClickListener {
            val requestBody = JSONObject()

            requestBody.put("album_name", albumName.text.toString())
            requestBody.put("album_description", albumDescription.text.toString())
            requestBody.put("album_genre", albumGenre.text.toString())
            requestBody.put("album_media", "hardcodeado") //todo completar con el storageName cuando este implementada el upload de imagenes
            requestBody.put("suscription", "free") //todo agregar editText de tipo de suscription a la vista y extraer el dato
            requestBody.put("artist_id", app.getProfileData("id"))

            val url = "http://spotifiubyfy-music.herokuapp.com/albums"

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response -> val intent = Intent(this, SongCreationPage::class.java).apply {
                    putExtra("album_id", response.getString("id"))
                    putExtra("album_name", albumName.text.toString())
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