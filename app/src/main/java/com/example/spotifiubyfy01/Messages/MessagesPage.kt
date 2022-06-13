package com.example.spotifiubyfy01.Messages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.adapter.ArtistChatViewHolder
import com.example.spotifiubyfy01.Messages.adapter.ArtistChatsRecyclerAdapter
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.ReproductionPage
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack

class MessagesPage: AppCompatActivity(), VolleyCallBack<ChatBundle> {
    var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_page)
        initRecyclerView(ArrayList())
        val app = (this.application as Spotifiubify)
        userId = app.getProfileData("id")!!.toInt()
        MessagesDataSource.getChatsOfArtistWithID(this, userId!!, this)
    }

    private fun initRecyclerView(chatList: List<ChatBundle>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            ArtistChatsRecyclerAdapter(chatList as MutableList<ChatBundle>) { chatView, chatBundle, position ->
                onItemClicked(chatView, chatBundle, position)
            }
    }

    private fun onItemClicked(chatView: ArtistChatViewHolder, chatBundle: ChatBundle, position: Int) {
        chatBundle.chat_seen = true
        chatView.changeToSeen()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter!!.notifyItemChanged(position)
        val intent = Intent(this, ChatPage::class.java)
        intent.putExtra("requester_id", userId!!)
        intent.putExtra("other", chatBundle.artist)
        intent.putExtra("position", position)
        resultLauncher.launch(intent)
    }

    override fun updateData(list: List<ChatBundle>) {
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

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != -1) { //chats need to be updated
            val position = result.resultCode
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            (recyclerView.adapter as ArtistChatsRecyclerAdapter).putItemOfPositionOnTop(position)
        }
    }

}