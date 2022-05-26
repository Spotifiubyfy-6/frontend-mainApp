package com.example.spotifiubyfy01

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifiubyfy01.artistProfile.Album

class AlbumPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_page)
        val album = intent.extras?.get("Album") as Album
        findViewById<TextView>(R.id.albumName).text = album.album_name
        /*val songs = JSONArray()

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

*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}