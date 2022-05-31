package com.example.spotifiubyfy01.search

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.Song
import org.json.JSONArray
import org.json.JSONObject

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"
var album_image_link = "https://ladydanville.files.wordpress.com/2012/03/blankart.png"

class SearchListMonitor {
    private val listToBeSent = ArrayList<SearchItem>()
    private var sharedCounter: Int = 0

    fun updateListAndCounterAndCallBackIfNeeded(auxList: ArrayList<SearchItem>,
                                                callBack: VolleyCallBack<SearchItem> ) {
        synchronized(this) {
            for (i in 0 until auxList.size)
                listToBeSent.add(auxList[i])
            sharedCounter++
            if (sharedCounter == 2)
                callBack.updateData(listToBeSent)
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
            val synchronizer = SearchListMonitor()
            fetchArtists(slice, context, callBack, synchronizer)
            fetchAlbums(slice, context, callBack, synchronizer)
        }

        private fun getArtist(jsonArtist: JSONObject): SearchItem {
            val username = jsonArtist.getString("username")
            val id = jsonArtist.getString("id").toInt()
            return Artist(id, username, image_link)
        }

        private fun fetchArtists(slice: String, context: Context,
                                 callBack: VolleyCallBack<SearchItem>,
                                 synchronizer: SearchListMonitor) {
            val auxList = ArrayList<SearchItem>()
            val url = "https://spotifiubyfy-users.herokuapp.com/users/information/" + slice +
                    "?skip=0&limit=10"
            val getRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                url, null,
                Response.Listener { response ->
                    for (i in 0 until response.length())
                        auxList.add(getArtist(JSONObject(response.get(i).toString())))
                    synchronizer.updateListAndCounterAndCallBackIfNeeded(auxList, callBack)
                },
                { errorResponse ->
                    /*   val intent = Intent(context, PopUpWindow::class.java).apply {
                           val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                           putExtra("popuptext", error)
                           putExtra("tokenValidation", true)
                       }
                       startActivity(intent)
                */
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    //    params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                    return HashMap()
                }
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun fetchAlbums(slice: String, context: Context,
            callBack: VolleyCallBack<SearchItem>, synchronizer: SearchListMonitor
        ) {
            val auxList = ArrayList<SearchItem>()
            val url = "https://spotifiubyfy-music.herokuapp.com/albums?q=" + slice +
                    "&skip=0&limit=100"
            val getRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                url, null,
                Response.Listener { response ->
                    for (i in 0 until response.length())
                        auxList.add(getAlbum("defaultArtist", JSONObject(response.get(i).toString())))
                    synchronizer.updateListAndCounterAndCallBackIfNeeded(auxList, callBack)
                },
                { errorResponse ->
                    /*   val intent = Intent(context, PopUpWindow::class.java).apply {
                           val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                           putExtra("popuptext", error)
                           putExtra("tokenValidation", true)
                       }
                       startActivity(intent)
                */
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    //    params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                    return HashMap()
                }
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun getSong(artist_name: String, jsonSong: JSONObject): Song {
            val songName = jsonSong.getString("song_name")
            val albumId = jsonSong.getString("album_id").toInt()
            val id = jsonSong.getString("id").toInt()
            val storageName = jsonSong.getString("storage_name")
            return Song(songName, artist_name, albumId, id, storageName)
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

        private fun getAlbum(artist_name: String, jsonAlbum: JSONObject): SearchItem {
            val albumName = jsonAlbum.getString("album_name")
            return Album(
                albumName,
                album_image_link, artist_name,
                getListOfSongs(
                    artist_name,
                    JSONArray(jsonAlbum.getString("songs").toString())
                )
            )
        }
    }
}