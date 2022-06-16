package com.example.spotifiubyfy01.Messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.Messages.adapter.MessagesRecyclerAdapter
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.ReproductionPage
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import java.time.LocalDateTime

class ChatPage: AppCompatActivity(), VolleyCallBack<MessageItem> {
    var requesterId: Int? = null
    var other: Artist? = null
    var updated: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)
        requesterId = intent.extras?.get("requester_id") as Int
        other = intent.extras?.get("other") as Artist
        initOtherArtistField()
        initRecyclerView(ArrayList())
        MessagesDataSource.getConversationBetween(this, requesterId!!, other!!.id, this)
        val artistProfile = findViewById<CardView>(R.id.artist_profile)
        artistProfile.setOnClickListener{
            val intent = Intent(this, ArtistPage::class.java)
            intent.putExtra("Artist", other)
            intent.putExtra("chat_in_stack", true)
            startActivity(intent)
        }
        val messageTextBox = findViewById<EditText>(R.id.message_text)
        val sendButton = findViewById<Button>(R.id.send_button)
        sendButton.setOnClickListener{
            updated = true
            MessagesDataSender.sendMessage(this, requesterId!!, other!!.id,
                messageTextBox.text.toString().trim(), this::addMessage)
            messageTextBox.text.clear()
        }
    }

    private fun initOtherArtistField() {
        val artistBox = findViewById<TextView>(R.id.artist_name)
        artistBox.setText(other!!.artistName)
        val image: ImageView = findViewById(R.id.artist_image)
        Glide.with(image.context).load(other!!.image).into(image)
    }

    private fun initRecyclerView(messagesList: ArrayList<MessageItem>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MessagesRecyclerAdapter(messagesList)
        recyclerView.smoothScrollToPosition((recyclerView.adapter as MessagesRecyclerAdapter).itemCount)
    }

    private fun addMessage(message: Message, date: LocalDateTime) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        (recyclerView.adapter as MessagesRecyclerAdapter).addMessage(message, date)
        recyclerView.smoothScrollToPosition((recyclerView.adapter as MessagesRecyclerAdapter).itemCount)
    }

    override fun updateData(list: List<MessageItem>) {
        val arrayList = ArrayList<MessageItem>(list)
        initRecyclerView(arrayList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (updated)
                setResult(intent.extras?.get("position") as Int, intent);
            else
                setResult(-1, intent);
            finish()
            return true
        }
        if (item.itemId == R.id.action_playback) {
            startActivity(Intent(this, ReproductionPage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}