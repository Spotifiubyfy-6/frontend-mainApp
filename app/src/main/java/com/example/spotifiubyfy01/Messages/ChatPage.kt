package com.example.spotifiubyfy01.Messages

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.Messages.adapter.ArtistChatsRecyclerAdapter
import com.example.spotifiubyfy01.Messages.adapter.MessagesRecyclerAdapter
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.ReproductionPage
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack

class ChatPage: AppCompatActivity(), VolleyCallBack<Message> {
    var requesterId: Int? = null
    var other: Artist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)
        requesterId = intent.extras?.get("requester_id") as Int
        other = intent.extras?.get("other") as Artist
        initOtherArtistField()
        initRecyclerView(ArrayList())
        MessagesDataSource.getConversationBetween(this, requesterId!!, other!!.id, this)
    }

    private fun initOtherArtistField() {
        val artistBox = findViewById<TextView>(R.id.artist_name)
        artistBox.setText(other!!.username)
        val image: ImageView = findViewById(R.id.artist_image)
        Glide.with(image.context).load(other!!.image).into(image)
    }

    private fun initRecyclerView(messagesList: List<Message>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MessagesRecyclerAdapter(messagesList)
    }

    override fun updateData(messageList: List<Message>) {
        initRecyclerView(messageList)
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