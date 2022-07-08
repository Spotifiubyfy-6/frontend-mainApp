package com.example.spotifiubyfy01

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.artistProfile.Song
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class PlaylistCreationPage : BaseActivity() {
    var playlistCoverFile: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_creation_page)

        val app = (this.application as Spotifiubify)

        val playlistName = findViewById<EditText>(R.id.playlistName)
        val playlistDescription = findViewById<EditText>(R.id.playlistDescription)

        val findImage = findViewById<Button>(R.id.selectImage)
        findImage.setOnClickListener {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, 1)
        }
        val createPlaylistButton = findViewById<Button>(R.id.createPlaylistPageButton)

        val playlistId = intent.extras?.get("id") as String?
        if (playlistId != null) {
            playlistName.setText(intent.extras?.get("name") as String)
            playlistDescription.setText(intent.extras?.get("description") as String)
            val storageName = intent.extras?.get("image") as String
            createPlaylistButton.text = "EDIT PLAYLIST"
            createPlaylistButton.setOnClickListener {
                val url = "http://spotifiubyfy-music.herokuapp.com/playlists/" + playlistId
                val jsonRequest: StringRequest = object : StringRequest(
                    Method.PUT, url, { response ->
                        if (playlistCoverFile != null) {
                            val coverRef =  app.getStorageReference().child(storageName)
                            val uploadTask = coverRef.putFile(playlistCoverFile!!)
                            uploadTask.addOnFailureListener {
                                Toast.makeText(app, "Cover not uploaded: ERROR", Toast.LENGTH_LONG).show()
                            }.addOnSuccessListener {
                                Toast.makeText(app, "Cover successfully uploaded", Toast.LENGTH_SHORT).show()
                            }
                        }
                        val intent = Intent(this, MainPage::class.java)
                        startActivity(intent)
                    },
                    { errorResponse ->
                        Toast.makeText(this, "Cant edit playlist right now", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getBodyContentType(): String {
                        return "application/json"
                    }

                    override fun getBody(): ByteArray {
                        val params2 = HashMap<String, String>()
                        params2["playlist_name"] = playlistName.text.toString()
                        params2["playlist_description"] = playlistDescription.text.toString()
                        return JSONObject(params2 as Map<String, String>).toString().toByteArray()
                    }
                }
                MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
            }
        } else {
            createPlaylistButton.setOnClickListener {
                val requestBody = JSONObject()

                val playlistNameText = playlistName.text.toString()
                requestBody.put("playlist_name", playlistNameText)
                requestBody.put("playlist_description", playlistDescription.text.toString())
                requestBody.put("playlist_media", playlistNameText)
                requestBody.put("user_id", app.getProfileData("id"))


                val url = "https://spotifiubyfy-music.herokuapp.com/playlists"

                val jsonRequest = JsonObjectRequest(
                    Request.Method.POST, url, requestBody,
                    { response ->
                        val intent = Intent(this, PlaylistPage::class.java).apply {
                            val playlist : Playlist = getPlaylist(response)
                            putExtra("Playlist", playlist)
                            if (playlistCoverFile != null) {
                                val storageName = "covers/"+response.getString("playlist_media")
                                val coverRef =  app.getStorageReference().child(storageName)
                                val uploadTask = coverRef.putFile(playlistCoverFile!!)
                                uploadTask.addOnFailureListener {
                                    Toast.makeText(app, "Cover not uploaded: ERROR", Toast.LENGTH_LONG).show()
                                }.addOnSuccessListener {
                                    Toast.makeText(app, "Cover successfully uploaded", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        startActivity(intent)},
                    { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
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
                        startActivity(intent)}
                )
                MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
            }
        }
    }
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 1
            && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                this.playlistCoverFile = uri
            }
        }
    }

    private fun getPlaylist(jsonPlaylist: JSONObject): Playlist {
        val playlistName = jsonPlaylist.getString("playlist_name")
        val playlistId = jsonPlaylist.getString("id")
        val playlistDescription = jsonPlaylist.getString("playlist_description")
        val storageName = "covers/"+jsonPlaylist.getString("playlist_media")
        val userName = (this.application as Spotifiubify).getProfileData("username") as String
        return Playlist(
            playlistId,
            playlistName, playlistDescription,
            storageName, userName,
            getListOfSongs(
                JSONArray(jsonPlaylist.getString("songs").toString())
            ), "not applicable", false)
    }

    private fun getSong( jsonSong: JSONObject): Song {
        val songName = jsonSong.getString("song_name")
        val albumId = jsonSong.getString("album_id").toInt()
        val id = jsonSong.getString("id").toInt()
        val storageName = jsonSong.getString("storage_name")
        val artistName = jsonSong.getString("artist_name")
        val albumCover = jsonSong.getString("album_media")

        val songSuscription = jsonSong.getString("album_suscription")
        return Song(songName, artistName, albumId, id, storageName, albumCover, false, songSuscription)
    }
    private fun getListOfSongs(jsonSongs: JSONArray): List<Song> {
        val songs = ArrayList<Song>()
        for (i in 0 until jsonSongs.length())
            songs.add(getSong(JSONObject(jsonSongs.get(i).toString())))
        return songs
    }

}
