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
import com.example.spotifiubyfy01.artistProfile.Playlist
import com.example.spotifiubyfy01.artistProfile.Song
import org.json.JSONArray
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
            requestBody.put("playlist_media", playlistMediaPath.text.toString())
            requestBody.put("user_id", app.getProfileData("id"))

            val url = "https://spotifiubyfy-music.herokuapp.com/playlists"

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response ->

                    val intent = Intent(this, PlaylistPage::class.java).apply {
                        val pĺaylist : Playlist = getPlaylist("default username" ,response)
                        putExtra("Playlist", pĺaylist)

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

    private fun getPlaylist(userName: String, jsonPlaylist: JSONObject): Playlist {
        val playlistName = jsonPlaylist.getString("playlist_name")
        val playlistId = jsonPlaylist.getString("id")
        val storageName = "covers/"+jsonPlaylist.getString("playlist_media")
        return Playlist(
            playlistId,
            playlistName,
            storageName, userName,
            getListOfSongs(
                JSONArray(jsonPlaylist.getString("songs").toString())
            )
        )
    }

    private fun getSong( jsonSong: JSONObject): Song {
        val songName = jsonSong.getString("song_name")
        val albumId = jsonSong.getString("album_id").toInt()
        val id = jsonSong.getString("id").toInt()
        val storageName = jsonSong.getString("storage_name")
        val artistName = jsonSong.getString("artist_name")
        val albumCover = jsonSong.getString("album_media")

        return Song(songName, artistName, albumId, id, storageName, albumCover)
    }
    private fun getListOfSongs(jsonSongs: JSONArray): List<Song> {
        val songs = ArrayList<Song>()
        for (i in 0 until jsonSongs.length())
            songs.add(getSong(JSONObject(jsonSongs.get(i).toString())))
        return songs
    }
}
