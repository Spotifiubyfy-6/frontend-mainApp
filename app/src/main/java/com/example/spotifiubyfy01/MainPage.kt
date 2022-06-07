package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.artistProfile.adapter.SongRecyclerAdapter
import com.example.spotifiubyfy01.artistProfile.adapterSongRecyclerAdapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchPage
import com.example.spotifiubyfy01.search.adapter.SearchRecyclerAdapter
import org.json.JSONArray
import org.json.JSONObject




class MainPage: AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_playback) {
            startActivity(Intent(this, ReproductionPage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        (this.application as Spotifiubify).setProfile()

        val profile = findViewById<Button>(R.id.profile_button)
        profile.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }

        fetchSongs()
        fetchAlbums()
        fetchArtist()

    }
    private fun initRecyclerViewSongs(listOfSong: List<Song>) {

        val recyclerViewSongs = findViewById<RecyclerView>(R.id.recycler_view_songs)
        recyclerViewSongs.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewSongs.adapter = SongRecyclerAdapter(listOfSong) {song ->
            onSongClicked(song)
        }
    }

    private fun initRecyclerViewAlbum(listOfAlbums: List<Album>) {
        val recyclerViewAlbums = findViewById<RecyclerView>(R.id.recycler_view_album)
        recyclerViewAlbums.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewAlbums.adapter = AlbumRecyclerAdapter(listOfAlbums) { album ->
            onAlbumClicked(album)
        }
    }

    private fun initRecyclerViewArtist(listOfArtist:  List<Artist>) {
        val recyclerViewArtist= findViewById<RecyclerView>(R.id.recycler_view_artist)
        recyclerViewArtist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewArtist.adapter = SearchRecyclerAdapter(listOfArtist) { artist ->
            onArtistClicked(artist as Artist)
        }
    }


    private fun onArtistClicked(artist: Artist) {
        val intent = Intent(this, ArtistPage::class.java)
        intent.putExtra("Artist", artist)
        startActivity(intent)
    }



    private fun onSongClicked(song: Song) {
        val app = (this.application as Spotifiubify)
        app.SongManager.play(song)
        Log.d(ContentValues.TAG, song.song_name +" with id " + song.id.toString() + " made by " + song.artist)
    }

    private fun onAlbumClicked(album: Album) {
        val intent = Intent(this, AlbumPage::class.java)
        intent.putExtra("Album", album)
        startActivity(intent)
    }




    private fun fetchArtist() {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/artists/a?skip=0&limit=10"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                initRecyclerViewArtist(getListOfArtist(response))
            },
            { errorResponse ->
                print(errorResponse)
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }



    private fun fetchSongs() {
        val url = "https://spotifiubyfy-music.herokuapp.com/music?skip=0&limit=100"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                initRecyclerViewSongs(getListOfSongs(response))
            },
            { errorResponse ->
                print(errorResponse)
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun fetchAlbums() {
        val url = "https://spotifiubyfy-music.herokuapp.com/albums?skip=0&limit=100"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                initRecyclerViewAlbum(getListOfAlbums(response))
            },
            { errorResponse ->
                print(errorResponse)
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

     private fun getListOfSongs(jsonSongs: JSONArray): List<Song> {
        val songs = ArrayList<Song>()
        for (i in 0 until jsonSongs.length())
            songs.add(getSong(JSONObject(jsonSongs.get(i).toString())))
        return songs
    }

    private fun getListOfArtist(jsonSongs: JSONArray): List<Artist> {
        val artists = ArrayList<Artist>()
        for (i in 0 until jsonSongs.length())
            artists.add(getArtist(JSONObject(jsonSongs.get(i).toString())))
        return artists
    }

    private fun getSong( jsonSong: JSONObject): Song {
        val songName = jsonSong.getString("song_name")
        val albumId = jsonSong.getString("album_id").toInt()
        val id = jsonSong.getString("id").toInt()
        val storageName = jsonSong.getString("storage_name")
        val artistName = jsonSong.getString("artist_name")

        return Song(songName, artistName, albumId, id, storageName)
    }

    private fun getArtist( jsonSong: JSONObject): Artist {

        val id = jsonSong.getString("id").toInt()
        val username = jsonSong.getString("name")
        return Artist(id, username  , "image_url")
    }
    private fun getListOfAlbums(response: JSONArray): List<Album> {
        val list = ArrayList<Album>()
        for (i in 0 until response.length())
            list.add(getAlbum(JSONObject(response.get(i).toString())))
        return list
    }

    private fun getAlbum(jsonAlbum: JSONObject): Album {
        val albumName = jsonAlbum.getString("album_name")
        val albumId = jsonAlbum.getString("id")
    	val storageName = "covers/"+jsonAlbum.getString("album_media")
        return Album(albumId, albumName, storageName, "artist_name",
            getListOfSongs(
                JSONArray(jsonAlbum.getString("songs").toString())
            )
        )
    }
}
