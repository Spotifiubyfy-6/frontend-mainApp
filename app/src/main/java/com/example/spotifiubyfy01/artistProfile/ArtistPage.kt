package com.example.spotifiubyfy01.artistProfile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.AlbumPage
import com.example.spotifiubyfy01.artistProfile.adapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack

class ArtistPage: AppCompatActivity(), VolleyCallBack<Album> {
    private var artist: Artist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_page)
        artist = intent.extras?.get("Artist") as Artist?
        if (artist == null) { //Artist activity has already been created and is in activities stack
            //savedInstanceState needs to exist
           val artistId =  (savedInstanceState?.getString("ArtistID") as String).toInt()
           val artistName = savedInstanceState?.getString("ArtistName") as String
           val artistImage = savedInstanceState?.getString("ArtistImage") as String
           artist = Artist(artistId, artistName, artistImage)
        }
        val artistName = findViewById<TextView>(R.id.artist_name)
        val image = findViewById<ImageView>(R.id.artist_image)
        artistName.text = artist!!.username //Use !! because at this point artist is not null
        Glide.with(image.context).load(artist!!.image).into(image)
        initRecyclerView(ArrayList())
        AlbumDataSource.createAlbumList(this, artist!!.id, artist!!.username,this)
    }

    private fun initRecyclerView(albumList: List<Album>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                                    false)
        recyclerView.adapter =
            AlbumRecyclerAdapter(albumList) {album ->
                onItemClicked(album)
            }
    }

    override fun updateDataInRecyclerView(list: List<Album>) {
        initRecyclerView(list)
    }

    private fun onItemClicked(album: Album) {
        val intent = Intent(this, AlbumPage::class.java)
        intent.putExtra("Album", album)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("ArtistId", artist!!.id.toString())
        outState.putString("ArtistName", artist!!.username)
        outState.putString("ArtistImage", artist!!.image)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}