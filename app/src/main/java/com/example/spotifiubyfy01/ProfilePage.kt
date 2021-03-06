package com.example.spotifiubyfy01

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.AlbumDataSource.Companion.createAlbumList
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.artistProfile.adapter.default_album_image
import com.example.spotifiubyfy01.artistProfile.adapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONArray
import org.json.JSONObject

class ProfilePage : BaseActivity(), VolleyCallBack<Album> {
    private var artist: Artist? = null
    var itemDeleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val app = (this.application as Spotifiubify)
        artist = intent.extras?.get("Artist") as Artist?
        if (artist == null) {
            artist = Artist(app.getProfileData("id")!!.toInt(), app.getProfileData("name")!!, "profilePictures"+app.getProfileData("username"))
        }
        findViewById<TextView>(R.id.artist_name).text = artist!!.artistName

        if ( !intent.getStringExtra("passwordChangeSuccess").isNullOrEmpty()) {
            Toast.makeText(this, intent.getStringExtra("passwordChangeSuccess"),Toast.LENGTH_LONG).show()
        }

        val editClick = findViewById<Button>(R.id.editButton)
        editClick.setOnClickListener {
            val intent = Intent(this, ProfileEditPage::class.java)
            startActivity(intent)
        }

        val createAlbumClick = findViewById<Button>(R.id.createAlbumButton)
        createAlbumClick.setOnClickListener {
            val intent = Intent(this, AlbumCreationPage::class.java)
            startActivity(intent)
        }

        val walletClick = findViewById<Button>(R.id.wallet)
        walletClick.setOnClickListener {
            val intent = Intent(this, Wallet::class.java)
            startActivity(intent)
        }

        val createSongButton = findViewById<Button>(R.id.createSongButton)
        createSongButton.setOnClickListener {
            val recyclerView = findViewById<RecyclerView>(R.id.album_recycler_view)
            val adapter = recyclerView.adapter as AlbumRecyclerAdapter
            if (adapter.itemCount == 0) {
                Toast.makeText(this@ProfilePage,
                    "You do not have any albums. Create an album first.",
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent(this, SongCreationPage::class.java)
            intent.putExtra("albums", ArrayList(adapter.albumList))
            startActivity(intent)
        }
	
	    val tempClick = findViewById<Button>(R.id.createPlaylistButton)
        tempClick.setOnClickListener {
            val intent = Intent(this, PlaylistCreationPage::class.java)
            startActivity(intent)
        }
        val image = findViewById<ImageView>(R.id.artist_image)
        val coverRef = app.getStorageReference().child(artist!!.image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(default_album_image).into(image)
        }
        createAlbumList(this, artist!!.id, artist!!.artistName, true,this)

        val followingBtn = findViewById<Button>(R.id.following)
        followingBtn.setOnClickListener {
            val intent = Intent(this, Following::class.java)
            startActivity(intent)
        }

        fetchMyPlaylists()
    }

    private fun initAlbumRecyclerView(albumList: MutableList<Album>) {
        if (albumList.isEmpty()) {
            val text: TextView = findViewById(R.id.albumInformationText)
            text.visibility = VISIBLE
        }
        val recyclerView = findViewById<RecyclerView>(R.id.album_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerView.adapter =
            AlbumRecyclerAdapter(albumList, this::onItemClicked, this::onDeleteButtonClicked)
    }

    private fun onDeleteButtonClicked(album: Album, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Do you want to delete this album?")
        alertDialogBuilder.setMessage("This action is irreversible.")
        alertDialogBuilder.setNegativeButton("yes") { _, _ ->
            DeleteSender.deleteAlbum(this, album.album_id, position, this::onAlbumDeletion)
        }
        alertDialogBuilder.setPositiveButton("no", null)
        alertDialogBuilder.show()
    }

    private fun onAlbumDeletion(position: Int) {
        val recyclerView = findViewById<RecyclerView>(R.id.album_recycler_view)
        (recyclerView.adapter as AlbumRecyclerAdapter).deleteItemOfPosition(position)
        itemDeleted = true
    }

    private fun onItemClicked(album: Album) {
        val intent = Intent(this, AlbumPage::class.java)
        intent.putExtra("Album", album)
        intent.putExtra("ownAlbum", true)
        startActivity(intent)
    }

    override fun updateData(list: List<Album>) {
        initAlbumRecyclerView(list as MutableList<Album>)
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
                val ownedPlaylists = ArrayList<Playlist>()
                val collaboratingPlaylists = ArrayList<Playlist>()
                getListOfPlaylists(ownedPlaylists, collaboratingPlaylists, response)
                initRecyclerViewPlaylists(ownedPlaylists, collaboratingPlaylists)
            },
            { errorResponse ->
                print(errorResponse)
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun initRecyclerViewPlaylists(
        ownedPlaylists: List<Playlist>,
        collaboratingPlaylists: List<Playlist>
    ) {
        if (ownedPlaylists.isEmpty()) {
            val text: TextView = findViewById(R.id.playlistInformationText)
            text.visibility = VISIBLE
        }
        val recyclerViewPlaylist = findViewById<RecyclerView>(R.id.playlist_recycler_view)
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewPlaylist.adapter = PlaylistRecyclerAdapter(ownedPlaylists, this::onOwnedPlaylistClicked, this::onDeleteButtonClicked)

        if (collaboratingPlaylists.isEmpty()) {
            val text: TextView = findViewById(R.id.collaborationPlaylistInformationText)
            text.visibility = VISIBLE
        }
        val recyclerViewPlaylist2 = findViewById<RecyclerView>(R.id.playlist_collaborator_recycler_view)
        recyclerViewPlaylist2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerViewPlaylist2.adapter = PlaylistRecyclerAdapter(collaboratingPlaylists, this::onPlaylistClicked, this::onDeleteButtonClicked)
    }

    private fun onDeleteButtonClicked(playlist: Playlist, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Do you want to delete this playlist?")
        alertDialogBuilder.setMessage("This action is irreversible.")
        alertDialogBuilder.setNegativeButton("yes") { _, _ ->
            DeleteSender.deletePlaylist(this, playlist.playlist_id, position, this::onPlaylistDeletion)
        }
        alertDialogBuilder.setPositiveButton("no", null)
        alertDialogBuilder.show()
    }

    private fun onPlaylistDeletion(position: Int) {
        val recyclerView = findViewById<RecyclerView>(R.id.playlist_recycler_view)
        (recyclerView.adapter as PlaylistRecyclerAdapter).deleteItemOfPosition(position)
        itemDeleted = true
    }

    private fun onOwnedPlaylistClicked(playlist: Playlist) {
        val intent = Intent(this, PlaylistPage::class.java)
        intent.putExtra("Playlist", playlist)
        intent.putExtra("owned", true)
        startActivity(intent)
    }

    private fun onPlaylistClicked(playlist : Playlist) {
        val intent = Intent(this, PlaylistPage::class.java)
        intent.putExtra("Playlist", playlist)
        startActivity(intent)
    }

    private fun getListOfPlaylists(owned: ArrayList<Playlist>, collaborating: ArrayList<Playlist>,
                                   jsonSongs: JSONArray) {
        for (i in 0 until jsonSongs.length()) {
            val jsonPlaylist = JSONObject(jsonSongs.get(i).toString())
            if (jsonPlaylist.get("type").equals("owned"))
                owned.add(getPlaylist(jsonPlaylist))
            else
                collaborating.add(getPlaylist(jsonPlaylist))
        }
    }

    private fun getPlaylist( jsonPlaylist: JSONObject): Playlist {

        val playlistName = jsonPlaylist.getString("playlist_name")
        val playlistDescription = jsonPlaylist.getString("playlist_description")
        val playlistImage = "covers/"+jsonPlaylist.getString("playlist_media").toString()
        val playlistId = jsonPlaylist.getString("id")
        val artistUsername = jsonPlaylist.getString("artist_username")
        val type = jsonPlaylist.getString("type")
        return Playlist(playlistId, playlistName, playlistDescription, playlistImage, artistUsername,
            getListOfSongs(
                JSONArray(jsonPlaylist.getString("songs").toString())
            ), type, true)
    }

    private fun getListOfSongs(jsonSongs: JSONArray): List<Song> {
        val songs = ArrayList<Song>()
        for (i in 0 until jsonSongs.length())
            songs.add(getSong(JSONObject(jsonSongs.get(i).toString())))
        return songs
    }

    private fun getSong( jsonSong: JSONObject): Song {
        val songName = jsonSong.getString("song_name")
        val albumId = jsonSong.getString("album_id").toInt()
        val id = jsonSong.getString("id").toInt()
        val storageName = jsonSong.getString("storage_name")
        val artistName = jsonSong.getString("artist_name")
        val albumCover = "covers/"+jsonSong.getString("album_media")

        val songSuscription = jsonSong.getString("album_suscription")
        return Song(songName, artistName, albumId, id, storageName, albumCover, false, songSuscription)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (itemDeleted) {
            startActivity(Intent(this, MainPage::class.java))
            true
        }
        return super.onOptionsItemSelected(item)
    }

}
