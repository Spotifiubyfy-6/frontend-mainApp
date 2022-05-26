package com.example.spotifiubyfy01.artistProfile

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONArray
import org.json.JSONObject

var image_link = "https://ladydanville.files.wordpress.com/2012/03/blankart.png"

class AlbumDataSource {
    companion object {
        fun createAlbumList(context: Context, artist_id: Int, artist_name: String,
                            callBack: VolleyCallBack<Album>) {
            val url = "https://spotifiubyfy-music.herokuapp.com/artists/" + artist_id +
                    "/albums?skip=0&limit=100"
            val getRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                url, null,
                Response.Listener { response ->
                    callBack.updateDataInRecyclerView(getListOfAlbums(artist_name, response))
                },
                { errorResponse ->
                    /*   val intent = Intent(context, PopUpWindow::class.java).apply {
                           val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                           putExtra("popuptext", error)
                           putExtra("tokenValidation", true)
                       }
                       startActivity(intent)
                */ }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    //    params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                    return HashMap()
                }
                }
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun getSong(jsonSong: JSONObject): Song {
            val songName = jsonSong.getString("song_name")
            val albumId = jsonSong.getString("album_id").toInt()
            val id = jsonSong.getString("id").toInt()
            val storageName = jsonSong.getString("storage_name")
            return Song(songName, albumId, id, storageName)
        }

        private fun getListOfSongs(jsonSongs: JSONArray): List<Song> {
            val songs = ArrayList<Song>()
            for (i in 0 until jsonSongs.length())
                songs.add(getSong(JSONObject(jsonSongs.get(i).toString())))
            return songs
        }

        private fun getAlbum(artist_name: String, jsonAlbum: JSONObject): Album {
            val albumName = jsonAlbum.getString("album_name")
            return Album(albumName, image_link, artist_name,
                getListOfSongs(JSONArray(jsonAlbum.getString("songs").toString())))
        }

        private fun getListOfAlbums(artist_name: String, response: JSONArray): List<Album> {
            val list = ArrayList<Album>()
            for (i in 0 until response.length())
                list.add(getAlbum(artist_name, JSONObject(response.get(i).toString())))
            return list
        }
    }

}
