package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.AlbumDataSource
import com.example.spotifiubyfy01.artistProfile.AlbumDataSource.Companion.createAlbumList
import com.example.spotifiubyfy01.artistProfile.adapterSongRecyclerAdapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack


class ProfilePage : AppCompatActivity(), VolleyCallBack<Album> {
    private var artist: Artist? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_playback) {
            startActivity(Intent(this, ReproductionPage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val app = (this.application as Spotifiubify)
        artist = intent.extras?.get("Artist") as Artist?
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
        Glide.with(image.context).load(artist!!.image).into(image)
        initAlbumRecyclerView(ArrayList())
        createAlbumList(this, artist!!.id, artist!!.artistName,this)
    }

    private fun initAlbumRecyclerView(albumList: List<Album>) {
        val recyclerView = findViewById<RecyclerView>(R.id.album_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        recyclerView.adapter =
            AlbumRecyclerAdapter(albumList) {album ->
                onItemClicked(album)
            }
    }

    private fun onItemClicked(album: Album) {
        val intent = Intent(this, AlbumPage::class.java)
        intent.putExtra("Album", album)
        startActivity(intent)
    }

    override fun updateData(list: List<Album>) {
        initAlbumRecyclerView(list)
    }
}
