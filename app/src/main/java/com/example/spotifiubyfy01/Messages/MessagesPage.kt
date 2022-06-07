package com.example.spotifiubyfy01.Messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.adapter.MessagesRecyclerAdapter
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.ReproductionPage
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.adapterSongRecyclerAdapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack

class MessagesPage: AppCompatActivity(), VolleyCallBack<Artist> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_page)
        initRecyclerView(ArrayList())
        MessagesDataSource.getChatsOfArtistWithID(this, 1, this)
    }

    private fun initRecyclerView(artistList: List<Artist>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            MessagesRecyclerAdapter(artistList) {album ->
                onItemClicked(album)
            }
    }

    private fun onItemClicked(artist: Artist) {
        Log.d("TAG", "clicked!")
    }

    override fun updateData(list: List<Artist>) {
        initRecyclerView(list)
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
}