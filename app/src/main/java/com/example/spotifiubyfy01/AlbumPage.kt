package com.example.spotifiubyfy01

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray
import org.json.JSONObject

class AlbumPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_page)

        val songs = JSONArray()

        val url = "http://spotifiubyfy-music.herokuapp.com/albums/"+"16"

        val getRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val responseJson = JSONObject(response)
                findViewById<TextView>(R.id.albumName).text = responseJson.getString("album_name")
                songs.put(responseJson.getJSONArray("songs"))
                findViewById<TextView>(R.id.songName).text = JSONObject(songs.getString(0).substring( 1, songs.getString(0).length - 1 )).getString("song_name")
            },
            { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                putExtra("popuptext", error)
            }
                startActivity(intent)})

        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)


        val playSongButton = findViewById<Button>(R.id.playButton)
        val app = (this.application as Spotifiubify)

        playSongButton.setOnClickListener {
            val storageName = "songs/"+JSONObject(songs.getString(0).substring( 1, songs.getString(0).length - 1 )).getString("storage_name")
            val songRef =  app.getStorageReference().child(storageName)
            songRef.downloadUrl.addOnSuccessListener { url ->
                    val mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(url.toString())
                    prepare() // might take long! (for buffering, etc)
                    start()
                }
            }.addOnFailureListener {
                // Handle any errors
                Toast.makeText(app, "cant play song", Toast.LENGTH_LONG).show()
            }
        }


    }
}