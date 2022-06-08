package com.example.spotifiubyfy01.artistProfile

import android.content.Context
import android.content.Intent
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.PopUpWindow
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONArray
import org.json.JSONObject

class AlbumDataSource {
    companion object {
        fun createAlbumList(context: Context, artist_id: Int, artist_name: String,
                            callBack: VolleyCallBack<Album>) {
            val url = "https://spotifiubyfy-music.herokuapp.com/artists/" + artist_id +
                    "/albums?skip=0&limit=100"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                Response.Listener { response ->
                    callBack.updateData(getListOfAlbums(artist_name, response))
                },
                { val intent = Intent(context, PopUpWindow::class.java).apply {
//                    val error = errorResponse//.networkResponse.data.decodeToString() //.split('"')[3]
                    putExtra("popuptext", "cant create album right now")
                }
                    context.startActivity(intent)})
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun getSong(artist_name: String, jsonSong: JSONObject): Song {
            val songName = jsonSong.getString("song_name")
            val albumId = jsonSong.getString("album_id").toInt()
            val id = jsonSong.getString("id").toInt()
            val storageName = jsonSong.getString("storage_name")
            val albumCover = "covers/"+jsonSong.getString("album_media")

            return Song(songName, artist_name, albumId, id, storageName, albumCover)
        }

        private fun getListOfSongs(artist_name: String, jsonSongs: JSONArray): List<Song> {
            val songs = ArrayList<Song>()
            for (i in 0 until jsonSongs.length())
                songs.add(getSong(artist_name, JSONObject(jsonSongs.get(i).toString())))
            return songs
        }

        private fun getAlbum(artist_name: String, jsonAlbum: JSONObject): Album {
            val albumName = jsonAlbum.getString("album_name")
            val albumId = jsonAlbum.getString("id")
	        val storageName = "covers/"+jsonAlbum.getString("album_media")
            val description = jsonAlbum.getString("album_description")
            val genre = jsonAlbum.getString("album_genre")
            return Album(albumId, albumName, storageName, artist_name,
                getListOfSongs(artist_name, JSONArray(jsonAlbum.getString("songs").toString())),
                description, genre)
        }

        private fun getListOfAlbums(artist_name: String, response: JSONArray): List<Album> {
            val list = ArrayList<Album>()
            for (i in 0 until response.length())
                list.add(getAlbum(artist_name, JSONObject(response.get(i).toString())))
            return list
        }
    }

}
