package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.search.adapter.SearchRecyclerAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class ListOfPlaylistsPage : BaseActivity() {



    private var songID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_playlists_page)

        songID = intent.getIntExtra("song_to_add_id", 0)

        fetchMyPlaylists()
    }

    private fun initRecyclerViewPlaylists(listOfPlaylists:  List<Playlist>) {
        val recyclerViewPlaylist = findViewById<RecyclerView>(R.id.recycler_view_playlist)
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
            false)
        recyclerViewPlaylist.adapter = SearchRecyclerAdapter(listOfPlaylists) { playlist ->
            onPlaylistClicked(playlist as Playlist)
        }
    }


    private fun onPlaylistClicked(playlist: Playlist) {

        val playlistID = playlist.playlist_id

        val url = "https://spotifiubyfy-music.herokuapp.com/playlists/$playlistID/tracks?song_id=$songID"
        Log.d(ContentValues.TAG, "EL URL$url")

        val getRequest = JsonObjectRequest(
            Request.Method.POST,url,null,
            { response ->
                Log.d(ContentValues.TAG, "AGREGADO EXISTOSO: $response")
                fetchPlaylistById(playlistID)

            },

            { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                var body = "undefined error"
                Log.d(ContentValues.TAG, "AGREGADO ERRONEO: $errorResponse")
                if (errorResponse.networkResponse.data != null) {
                    try {
                        body = String(errorResponse.networkResponse.data, Charsets.UTF_8)
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
                putExtra("popuptext", body)
            }
                startActivity(intent)})
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }


    private fun getListOfPlaylist(response: JSONArray): List<Playlist> {
        val list = ArrayList<Playlist>()
        for (i in 0 until response.length())
            list.add(getPlaylist(JSONObject(response.get(i).toString())))
        return list
    }

    private fun getPlaylist(jsonPlaylist: JSONObject): Playlist {
        val playlistID = jsonPlaylist.getString("id")
        val playlistName = jsonPlaylist.getString("playlist_name")
        val description = jsonPlaylist.getString("playlist_description")
        val storageName = "covers/"+jsonPlaylist.getString("playlist_media")
        val userName = (this.application as Spotifiubify).getProfileData("username") as String
        val songs = ArrayList<Song>()

        return Playlist(playlistID, playlistName, description, storageName, userName, songs, "not applicable",false)
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
    private fun getPlaylist(userName: String, jsonPlaylist: JSONObject): Playlist {
        val playlistName = jsonPlaylist.getString("playlist_name")
        val playlistId = jsonPlaylist.getString("id")
        val storageName = "covers/"+jsonPlaylist.getString("playlist_media")
        val realUserName = jsonPlaylist.getString("artist_username")
        val description = jsonPlaylist.getString("playlist_description")
        return Playlist(
            playlistId,
            playlistName, description,
            storageName, realUserName,
            getListOfSongs(
                JSONArray(jsonPlaylist.getString("songs").toString())
            ), "not applicable", false)
    }

    private fun fetchPlaylistById(playlistID : String) {
        val url = "https://spotifiubyfy-music.herokuapp.com/playlists/$playlistID"

        val getRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->



                val playlist : Playlist = getPlaylist("default username" ,response)

                val intent = Intent(this, PlaylistPage::class.java).apply {
                    putExtra("Playlist", playlist)
                }
                startActivity(intent)
            },
            { errorResponse ->
                print(errorResponse)
                Log.d(ContentValues.TAG, "Unable to fetch playlist by id: $errorResponse")
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun fetchMyPlaylists() {
        val app = (this.application as Spotifiubify)
        val userId = app.getProfileData("id")
        val url = "https://spotifiubyfy-music.herokuapp.com/users/$userId/playlists?skip=0&limit=100"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                initRecyclerViewPlaylists(getListOfPlaylist(response))
            },
            { errorResponse ->
                print(errorResponse)
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }
}