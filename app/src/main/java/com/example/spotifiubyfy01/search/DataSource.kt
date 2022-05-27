package com.example.spotifiubyfy01.search

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.AlbumDataSource
import com.example.spotifiubyfy01.artistProfile.Song
import org.json.JSONArray
import org.json.JSONObject

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"
var album_image_link = "https://ladydanville.files.wordpress.com/2012/03/blankart.png"

class DataSource {
    companion object {
        fun updateDataSet(context: Context, slice: String, callBack: VolleyCallBack<SearchItem>) {
            val list = ArrayList<SearchItem>()
            if (slice.isEmpty()) {
                callBack.updateDataInRecyclerView(list)
                return
            }
            fetchArtists(list, slice, context)
            fetchAlbums(list, slice, context, callBack)
        }

        private fun getArtist(jsonArtist: JSONObject): SearchItem {
            val username = jsonArtist.getString("username")
            val id = jsonArtist.getString("id").toInt()
            return Artist(id, username, image_link)
        }

        private fun fetchArtists(list: ArrayList<SearchItem>, slice: String, context: Context) {
            val url = "https://spotifiubyfy-users.herokuapp.com/users/information/" + slice +
                    "?skip=0&limit=10"
            val getRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                url, null,
                Response.Listener { response ->
                    for (i in 0 until response.length())
                        list.add(getArtist(JSONObject(response.get(i).toString())))
                    Log.d(TAG, "artists fetched")
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

        private fun fetchAlbums(
            list: ArrayList<SearchItem>, slice: String, context: Context,
            callBack: VolleyCallBack<SearchItem>
        ) {
            val url = "https://spotifiubyfy-music.herokuapp.com/albums?q=" + slice +
                    "&skip=0&limit=100"
            val getRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                url, null,
                Response.Listener { response ->
                    Log.d(TAG, "fetching albums")
                    for (i in 0 until response.length())
                        list.add(getAlbum("defaultArtist", JSONObject(response.get(i).toString())))
                    callBack.updateDataInRecyclerView(list)
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
            /* list.add(Album("Rubber Soul", album_image_link, "The Beatles", ArrayList<Song>()))
            list.add(Album("Revolver", album_image_link, "The Beatles", ArrayList<Song>()))
            list.add(Album("Led Zeppelin I", album_image_link, "Led Zeppelin", ArrayList<Song>()))
            list.add(Album("Hold on, we are going down", album_image_link, "Drake", ArrayList<Song>()))
        */
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
                com.example.spotifiubyfy01.artistProfile.image_link, artist_name,
                getListOfSongs(
                    artist_name,
                    JSONArray(jsonAlbum.getString("songs").toString())
                )
            )
        }
    }
}