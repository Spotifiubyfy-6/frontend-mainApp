package com.example.spotifiubyfy01.search

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.Playlist
import com.example.spotifiubyfy01.artistProfile.Song
import org.json.JSONArray
import org.json.JSONObject

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

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
            fetchByArtistsAsFilter(slice, context, callBack, synchronizer)
            fetchAlbums(slice, context, callBack, synchronizer)
            fetchPlaylists(slice, context, callBack, synchronizer)
        }

        private fun getArtist(jsonArtist: JSONObject): SearchItem {
            val username = jsonArtist.getString("name")
            val id = jsonArtist.getString("id").toInt()
            return Artist(id, username, image_link)
        }

        private fun fetchByArtistsAsFilter(slice: String, context: Context,
                                 callBack: VolleyCallBack<SearchItem>,
                                 synchronizer: SearchListMonitor) {
            val auxList = ArrayList<SearchItem>()
            val url = "https://spotifiubyfy-music.herokuapp.com/search/" + slice
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    for (i in 0 until response.length())
                        insertArtistAlbumsAndSongsToList(auxList, JSONObject(response.get(i).toString()))
                    synchronizer.updateListAndCounterAndCallBackIfNeeded(auxList, callBack)
                },
                {

                })
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun insertArtistAlbumsAndSongsToList(
            auxList: ArrayList<SearchItem>,
            jsonArtistWithSongsNAlbums: JSONObject
        ) {
            auxList.add(getArtist(jsonArtistWithSongsNAlbums))
            addAlbumsToList(auxList, jsonArtistWithSongsNAlbums.get("albums") as JSONArray)
            //addSongsToList(auxList, jsonArtistWithSongsNAlbums.get("songs") as JSONArray,
              //  jsonArtistWithSongsNAlbums.get("name") as String)
        }

        private fun addSongsToList(
            auxList: java.util.ArrayList<SearchItem>,
            songsJsonArray: JSONArray,
            artistName: String
        ) {
            //for (i in 0 until songsJsonArray.length())
               // auxList.add(getSong(artistName, JSONObject(songsJsonArray.get(i).toString())))
        }

        private fun addAlbumsToList(auxList: ArrayList<SearchItem>, albumsJsonArray: JSONArray) {
            for (i in 0 until albumsJsonArray.length())
                auxList.add(getAlbum(JSONObject(albumsJsonArray.get(i).toString())))
        }

        private fun fetchPlaylists(slice: String, context: Context,
                                 callBack: VolleyCallBack<SearchItem>,
                                 synchronizer: SearchListMonitor) {
            val auxList = ArrayList<SearchItem>()

            val url = "https://spotifiubyfy-music.herokuapp.com/playlists?q=$slice&skip=0&limit=5"

            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    for (i in 0 until response.length())
                        auxList.add(getPlaylist("default_username" ,JSONObject(response.get(i).toString())))
                    synchronizer.updateListAndCounterAndCallBackIfNeeded(auxList, callBack)
                },
                {

                })
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        private fun fetchAlbums(slice: String, context: Context,
            callBack: VolleyCallBack<SearchItem>, synchronizer: SearchListMonitor
        ) {
            val auxList = ArrayList<SearchItem>()
            val url = "https://spotifiubyfy-music.herokuapp.com/albums?q=" + slice +
                    "&skip=0&limit=100"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    for (i in 0 until response.length())
                        auxList.add(getAlbum(JSONObject(response.get(i).toString())))
                    synchronizer.updateListAndCounterAndCallBackIfNeeded(auxList, callBack)
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

            return Song(songName, artist_name, albumId, id, storageName, albumCover)
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
        var artistName = "default artist name"
        if (songs.length() > 0) {
            val song = songs.getJSONObject(0)
            artistName = song.getString("artist_name")
        }
        val description = jsonAlbum.getString("album_description")
        val genre = jsonAlbum.getString("album_genre")
            return Album(
                albumId,
                albumName,
                storageName, artistName,
                getListOfSongs(
                    artistName,
                    JSONArray(jsonAlbum.getString("songs").toString())
                ), description, genre
            )
        }

        private fun getPlaylist(userName: String, jsonPlaylist: JSONObject): SearchItem {
            val playlistName = jsonPlaylist.getString("playlist_name")
            val playlistId = jsonPlaylist.getString("id")
            val storageName = "covers/"+jsonPlaylist.getString("playlist_media")
            return Playlist(
                playlistId,
                playlistName,
                storageName, userName,
                getListOfSongs(
                    userName,
                    JSONArray(jsonPlaylist.getString("songs").toString())
                )
            )
        }
    }
}
