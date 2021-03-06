package com.example.spotifiubyfy01.search

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.Playlist
import com.example.spotifiubyfy01.PopUpWindow
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.Song
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import kotlin.reflect.KFunction1


class SearchListMonitor(private val countTo: Int) {
    private val listToBeSent = ArrayList<SearchItem>()
    private var sharedCounter: Int = 0

    fun updateCounterAndCallBackIfNeeded(callBack: VolleyCallBack<SearchItem>) {
        synchronized(this) {
            sharedCounter++
            if (sharedCounter == countTo)
                callBack.updateData(listToBeSent)
        }
    }
    fun updateList(auxList: ArrayList<SearchItem>) {
        synchronized(this) {
            for (i in 0 until auxList.size)
                listToBeSent.add(auxList[i])
        }
    }
}

class DataSource {
    companion object {
        fun updateDataSet(context: Context, slice: String, callBack: VolleyCallBack<SearchItem>) {
            if (slice.isEmpty()) {
                callBack.updateData(ArrayList())
                return
            }
            val synchronizer = SearchListMonitor(5)
            fetchByArtistsAsFilter(slice, context, callBack, synchronizer)
            fetchAlbumsBySlice(slice, context, callBack, synchronizer, 0)
            fetchAlbumsBySlice(slice, context, callBack, synchronizer, 1)
            fetchAlbumsBySlice(slice, context, callBack, synchronizer, 2)
            fetchPlaylistsBySlice(slice, context, callBack, synchronizer)
        }

        private fun getArtist(jsonArtist: JSONObject): SearchItem {
            val username = jsonArtist.getString("name")
            val id = jsonArtist.getString("id").toInt()
            val image = "profilePictures/"+jsonArtist.getString("username")
            return Artist(id, username, image)
        }

        private fun fetchByArtistsAsFilter(slice: String, context: Context,
                                 callBack: VolleyCallBack<SearchItem>,
                                 synchronizer: SearchListMonitor) {
            val url = "https://spotifiubyfy-music.herokuapp.com/search/$slice"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    for (i in 0 until response.length()) {
                        val auxList = ArrayList<SearchItem>()
                        insertArtistAlbumsAndSongsToList(auxList, JSONObject(response.get(i).toString()))
                        synchronizer.updateList(auxList)
                    }
                    synchronizer.updateCounterAndCallBackIfNeeded(callBack)
                },
                { errorResponse -> val intent = Intent(context, PopUpWindow::class.java).apply {
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
                    startActivity(context, intent, null)
                })
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun insertArtistAlbumsAndSongsToList(
            auxList: ArrayList<SearchItem>,
            jsonArtistWithSongsNAlbums: JSONObject
        ) {
            auxList.add(getArtist(jsonArtistWithSongsNAlbums))
            addAlbumsNSongsToList(auxList, jsonArtistWithSongsNAlbums.get("albums") as JSONArray,
                jsonArtistWithSongsNAlbums.get("name") as String)
        }

        private fun addSongsToList(
            auxList: java.util.ArrayList<SearchItem>,
            songsJsonArray: JSONArray,
            artistName: String
        ) {
            var maxNumberOfSongs = songsJsonArray.length()
            if (maxNumberOfSongs > 2)
                maxNumberOfSongs = 2
            for (i in 0 until maxNumberOfSongs)
                auxList.add(getSong(artistName, JSONObject(songsJsonArray.get(i).toString())))
        }

        private fun addAlbumsNSongsToList(auxList: ArrayList<SearchItem>, albumsJsonArray: JSONArray,
                                            artistName: String) {
            var maxNumberOfAlbums = albumsJsonArray.length()
            if (maxNumberOfAlbums > 2)
                maxNumberOfAlbums = 2
            for (i in 0 until maxNumberOfAlbums) {
                val jsonAlbum = JSONObject(albumsJsonArray.get(i).toString())
                auxList.add(getAlbum(jsonAlbum))
                addSongsToList(auxList, jsonAlbum.get("songs") as JSONArray, artistName)
            }
        }

        private fun fetchPlaylistsBySlice(slice: String, context: Context,
                                          callBack: VolleyCallBack<SearchItem>,
                                          synchronizer: SearchListMonitor) {
            val auxList = ArrayList<SearchItem>()

            val url = "https://spotifiubyfy-music.herokuapp.com/playlists?q=$slice&skip=0&limit=5"

            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    for (i in 0 until response.length())
                        auxList.add(getPlaylist(JSONObject(response.get(i).toString())))
                    synchronizer.updateList(auxList)
                    synchronizer.updateCounterAndCallBackIfNeeded(callBack)
                },
                {

                })
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun fetchAlbumsBySlice(
            slice: String, context: Context,
            callBack: VolleyCallBack<SearchItem>, synchronizer: SearchListMonitor,
            field: Int
        ) {
            val auxList = ArrayList<SearchItem>()
            val url: String
            url = if (field == 0) {
                "http://spotifiubyfy-music.herokuapp.com/albums?genre=" + slice + "&skip=0&limit=100"
            } else if (field == 1){
                "https://spotifiubyfy-music.herokuapp.com/albums?q=" + slice +
                        "&skip=0&limit=100"
            } else {
                "https://spotifiubyfy-music.herokuapp.com/albums?suscription=" + slice +
                        "&skip=0&limit=100"
            }
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    for (i in 0 until response.length())
                        auxList.add(getAlbum(JSONObject(response.get(i).toString())))
                    synchronizer.updateList(auxList)
                    synchronizer.updateCounterAndCallBackIfNeeded(callBack)
                }
            ) {
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun getSong(artist_name: String, jsonSong: JSONObject): Song {
            val songName = jsonSong.getString("song_name")
            val albumId = jsonSong.getString("album_id").toInt()
            val id = jsonSong.getString("id").toInt()
            val storageName = jsonSong.getString("storage_name")
            val albumCover = "covers/"+jsonSong.getString("album_media")
            val artistName = jsonSong.getString("artist_name")

            val songSuscription = jsonSong.getString("album_suscription")
            return Song(songName, artistName, albumId, id, storageName, albumCover, false, songSuscription)
        }

        private fun getListOfSongs(artist_name: String, jsonSongs: JSONArray): List<Song> {
            val songs = ArrayList<Song>()
            for (i in 0 until jsonSongs.length())
                songs.add(
                    getSong(
                        artist_name,
                        JSONObject(jsonSongs.get(i).toString())
                    )
                )
            return songs
        }

        private fun getAlbum(jsonAlbum: JSONObject): SearchItem {
            val albumName = jsonAlbum.getString("album_name")
            val albumId = jsonAlbum.getString("id")
            val storageName = "covers/"+jsonAlbum.getString("album_media")
            val songs = jsonAlbum.getJSONArray("songs")
            var artistName = jsonAlbum.getString("artist_name")
            if (songs.length() > 0) {
                val song = songs.getJSONObject(0)
                artistName = song.getString("artist_name")
            }
            val description = jsonAlbum.getString("album_description")
            val genre = jsonAlbum.getString("album_genre")
            val authorId = jsonAlbum.getString("artist_id")
            val suscription = jsonAlbum.getString("suscription")

            return Album(
                    albumId,
                    albumName,
                    storageName, artistName,
                    getListOfSongs(
                        artistName,
                        JSONArray(jsonAlbum.getString("songs").toString())
                    ), description, genre, suscription, authorId, false)
        }

        private fun getPlaylist(jsonPlaylist: JSONObject): SearchItem {
            val playlistName = jsonPlaylist.getString("playlist_name")
            val playlistId = jsonPlaylist.getString("id")
            val storageName = "covers/"+jsonPlaylist.getString("playlist_media")
            val playlistDescription = jsonPlaylist.getString("playlist_description")
            val userName = jsonPlaylist.getString("artist_username")
            return Playlist(
                playlistId,
                playlistName, playlistDescription,
                storageName, userName,
                getListOfSongs(
                    userName,
                    JSONArray(jsonPlaylist.getString("songs").toString())
                ), "not applicable", false)
        }

        fun updateDataSetOfArtist(context: Context, slice: String, callBack: VolleyCallBack<Artist>) {
            val list = ArrayList<Artist>()
            val url = "https://spotifiubyfy-users.herokuapp.com/users/artists/" + slice +
                    "?skip=0&limit=10"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    for (i in 0 until response.length())
                        list.add(getArtist(JSONObject(response.get(i).toString())) as Artist)
                    callBack.updateData(list)
                },
                {

                })
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        fun getAvailableSuscriptions(context: Context, onSuscriptionsObtained: KFunction1<List<String>, Unit>) {
            val url = "https://spotifiubyfy-users.herokuapp.com/users/user_subscription"
            val getRequest = JsonObjectRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    val suscriptionList = ArrayList<String>()
                    for (i in 0 until response.names().length()) {
                        suscriptionList.add(response.names().getString(i))
                    }
                    onSuscriptionsObtained(suscriptionList)
                },
                {
                    Toast.makeText(context, "Cant get suscriptions types right now", Toast.LENGTH_SHORT).show()
                })
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        fun getAvailableIntetests(context: Context, onInterestsObtained: (List<String>) -> Unit) {
            val url = "https://spotifiubyfy-users.herokuapp.com/interests"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val list = Array(response.length()) {
                        response.getString(it)
                    }
                    onInterestsObtained(list.toList())
                },
                {
                    Toast.makeText(context, "Cant get interests right now", Toast.LENGTH_SHORT).show()
                })
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)

        }
    }
}
