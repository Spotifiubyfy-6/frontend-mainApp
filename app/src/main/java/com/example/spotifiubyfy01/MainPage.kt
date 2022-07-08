package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.Messages.MessagesPage
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.artistProfile.adapter.SongRecyclerAdapter
import com.example.spotifiubyfy01.artistProfile.adapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchPage
import com.example.spotifiubyfy01.search.adapter.SearchRecyclerAdapter
import org.json.JSONArray
import org.json.JSONObject




class MainPage: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        NotificationCreator().setChannels(this)
        (this.application as Spotifiubify).setProfile()

        val profile = findViewById<Button>(R.id.profile_button)
        profile.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            val app = (this.application as Spotifiubify)
            val artist = Artist(app.getProfileData("id").toString().toInt(),
                                app.getProfileData("username").toString(),
                                "profilePictures/"+app.getProfileData("username").toString())
            intent.putExtra("Artist", artist)
            startActivity(intent)
        }

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }

        val messagesButton = findViewById<Button>(R.id.messages_button)
        messagesButton.setOnClickListener {
            val intent = Intent(this, MessagesPage::class.java)
            startActivity(intent)
        }

        fetchSongs()
        fetchAlbums()
        fetchArtist()
        fetchPlaylists()


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
        recyclerViewAlbums.adapter = AlbumRecyclerAdapter(listOfAlbums, { album ->
            onAlbumClicked(album)
        }, null)
    }

    private fun initRecyclerViewAlbumRecGeo(listOfAlbums: List<Album>) {
        val recyclerViewAlbums = findViewById<RecyclerView>(R.id.recycler_view_album_rec_geo)
        recyclerViewAlbums.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewAlbums.adapter = AlbumRecyclerAdapter(listOfAlbums, { album ->
            onAlbumClicked(album)
        }, null)
    }

    private fun initRecyclerViewArtist(listOfArtist:  List<Artist>) {
        val recyclerViewArtist= findViewById<RecyclerView>(R.id.recycler_view_artist)
        recyclerViewArtist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewArtist.adapter = SearchRecyclerAdapter(listOfArtist) { artist ->
            onArtistClicked(artist as Artist)
        }
    }

    private fun initRecyclerViewArtistNearYou(listOfArtist: List<Artist>) {
        val recyclerViewArtist= findViewById<RecyclerView>(R.id.recycler_view_rec_artist_geo)
        recyclerViewArtist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewArtist.adapter = SearchRecyclerAdapter(listOfArtist) { artist ->
            onArtistClicked(artist as Artist)
        }
    }

    private fun initRecyclerViewPLaylist(listOfPlaylists: List<Playlist>) {
        val recyclerViewPlaylist = findViewById<RecyclerView>(R.id.recycler_view_playlist)
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewPlaylist.adapter = PlaylistRecyclerAdapter(listOfPlaylists, this::onPlaylistClicked, null)
    }


    private fun initRecyclerViewAlbumRecGenre(listOfAlbums: List<Album>) {
        val recyclerViewAlbums = findViewById<RecyclerView>(R.id.recycler_view_album_rec_genre)
        recyclerViewAlbums.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewAlbums.adapter = AlbumRecyclerAdapter(listOfAlbums, { album ->
            onAlbumClicked(album)
        }, null)
    }

    private fun onArtistClicked(artist: Artist) {
        val intent = Intent(this, ArtistPage::class.java)
        intent.putExtra("Artist", artist)
        startActivity(intent)
    }

    private fun onPlaylistClicked(playlist : Playlist) {
        val intent = Intent(this, PlaylistPage::class.java)
        intent.putExtra("Playlist", playlist)
        startActivity(intent)
    }

    private fun onSongClicked(song: Song) {
        val app = (this.application as Spotifiubify)
        app.songManager.play(song)
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
            {
                Toast.makeText(this, "Cant fetch artists right now", Toast.LENGTH_SHORT).show()
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun fetchPlaylists() {
        val url = "https://spotifiubyfy-music.herokuapp.com/playlists?skip=0&limit=10"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                initRecyclerViewPLaylist(getListOfPlaylists(response))
            },
            {
                Toast.makeText(this, "Cant fetch playlists right now", Toast.LENGTH_SHORT).show()
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
            {
                Toast.makeText(this, "Cant fetch songs right now", Toast.LENGTH_SHORT).show()
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
                fetchAlbumsRecGeo()

            },
            {
                Toast.makeText(this, "Cant fetch albums right now", Toast.LENGTH_SHORT).show()
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun fetchAlbumsRecGeo() {

        val userId = (this.application as Spotifiubify).profileData["id"]

        val url = "https://spotifiubyfy-users.herokuapp.com/users/$userId/recommendations/geo?km=500"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                initRecyclerViewAlbumRecGeo(getListOfAlbums(response))
                fetchAlbumsRecGenre()
            },
            {
                Toast.makeText(this, "Cant fetch albums geo right now", Toast.LENGTH_SHORT).show()
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun fetchAlbumsRecGenre() {
        val userId = (this.application as Spotifiubify).profileData["id"]
        val url = "https://spotifiubyfy-users.herokuapp.com/users/$userId/recommendations/genre"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                initRecyclerViewAlbumRecGenre(getListOfAlbums(response))

                fetchArtistsNearYou()
            },
            {
                Toast.makeText(this, "Cant fetch albums genre right now", Toast.LENGTH_SHORT).show()
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun fetchArtistsNearYou() {
        val userId = (this.application as Spotifiubify).profileData["id"]
        val url = "https://spotifiubyfy-users.herokuapp.com/users/$userId/near_artists?km=5000&skip=0&limit=100"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                initRecyclerViewArtistNearYou(getListOfArtist(response))
            },
            {
                Toast.makeText(this, "Cant fetch artists right now", Toast.LENGTH_SHORT).show()
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

    private fun getListOfPlaylists(jsonSongs: JSONArray): List<Playlist> {
        val playlists = ArrayList<Playlist>()
        for (i in 0 until jsonSongs.length())
            playlists.add(getPlaylist(JSONObject(jsonSongs.get(i).toString())))
        return playlists
    }
    private fun getSong( jsonSong: JSONObject): Song {
        val songName = jsonSong.getString("song_name")
        val albumId = jsonSong.getString("album_id").toInt()
        val id = jsonSong.getString("id").toInt()
        val storageName = jsonSong.getString("storage_name")
        val artistName = jsonSong.getString("artist_name")
        val albumCover = "covers/"+jsonSong.getString("album_media")

        return Song(songName, artistName, albumId, id, storageName, albumCover)
    }

    private fun getArtist( jsonArtist: JSONObject): Artist {

        val id = jsonArtist.getString("id").toInt()
        val username = jsonArtist.getString("name")
        val artistImage = "profilePictures/"+jsonArtist.getString("username").toString()
        return Artist(id, username  , artistImage)
    }

    private fun getPlaylist( jsonPlaylist: JSONObject): Playlist {

        val playlistName = jsonPlaylist.getString("playlist_name")
        val playlistDescription = jsonPlaylist.getString("playlist_description")
        val playlistImage = "covers/"+jsonPlaylist.getString("playlist_media").toString()
        val playlistId = jsonPlaylist.getString("id")
        val artistUsername = jsonPlaylist.getString("artist_username")
        return Playlist(playlistId, playlistName,playlistImage,artistUsername,
            getListOfSongs(
                JSONArray(jsonPlaylist.getString("songs").toString())
            ), "not applicable" ,false)
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
        val songs = jsonAlbum.getJSONArray("songs")
        val description = jsonAlbum.getString("album_description")
        val genre = jsonAlbum.getString("album_genre")
        var artistName = "default artist name"
        val authorId = jsonAlbum.getString("artist_id")
        if (songs.length() > 0) {
            val song = songs.getJSONObject(0)
            artistName = song.getString("artist_name")
        }
        return Album(albumId, albumName, storageName, artistName,
            getListOfSongs(
                JSONArray(jsonAlbum.getString("songs").toString())
            ), description, genre, authorId, false)
    }
}
