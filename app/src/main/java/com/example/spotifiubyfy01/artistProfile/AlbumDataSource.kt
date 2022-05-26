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

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class AlbumDataSource {
    companion object {
        fun createAlbumList(context: Context, artist_id: Int, callBack: VolleyCallBack<Album>) {
            val list = ArrayList<Album>()
            val url = "https://spotifiubyfy-music.herokuapp.com/artists/" + artist_id +
                    "/albums?skip=0&limit=100"
            val getRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                url, null,
                Response.Listener { response ->
                    for (i in 0 until response.length()) {
                        val jsonAlbum = JSONObject(response.get(i).toString())
                        val albumName = jsonAlbum.getString("album_name")
                        val songs = ArrayList<Song>()
                        val jsonSongs = JSONArray(jsonAlbum.getString("songs").toString())
                        for (i in 0 until jsonSongs.length()) {
                            val jsonSong = JSONObject(jsonSongs.get(i).toString())
                            val songName = jsonSong.getString("song_name")
                            val album_id = jsonSong.getString("album_id").toInt()
                            val id = jsonSong.getString("id").toInt()
                            val storageName = jsonSong.getString("storage_name")
                            songs.add(Song(songName, album_id, id, storageName))
                        }
                        list.add(Album(albumName, image_link, songs))
                    }
                    callBack.updateDataInRecyclerView(list)
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
    }
}
