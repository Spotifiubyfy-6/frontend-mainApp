package com.example.spotifiubyfy01.Messages

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.adapter.ArtistChatViewHolder
import com.example.spotifiubyfy01.Messages.adapter.ArtistChatsRecyclerAdapter
import com.example.spotifiubyfy01.BaseActivity
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.search.SearchArtistPage
import com.example.spotifiubyfy01.search.VolleyCallBack

class MessagesPage: BaseActivity(), VolleyCallBack<ChatBundle> {
    var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_page)
        val app = (this.application as Spotifiubify)
        userId = app.getProfileData("id")!!.toString().toInt()
        MessagesDataSource.getChatsOfArtistWithID(this, userId!!, this)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = VISIBLE
        val messageNewArtistButton = findViewById<Button>(R.id.searchArtistToMessageButton)
        messageNewArtistButton.setOnClickListener {
            val intent = Intent(this, SearchArtistPage::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun initRecyclerView(chatList: List<ChatBundle>) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = GONE
        if (chatList.isEmpty())
            findViewById<TextView>(R.id.informationText).visibility = VISIBLE
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            ArtistChatsRecyclerAdapter(chatList as MutableList<ChatBundle>) { chatView, chatBundle, position ->
                onItemClicked(chatView, chatBundle, position)
            }
    }

    private fun onItemClicked(chatView: ArtistChatViewHolder, chatBundle: ChatBundle, position: Int) {
        chatBundle.number_of_not_seen = 0
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

    fun refreshChats() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = VISIBLE
        MessagesDataSource.getChatsOfArtistWithID(this, userId!!, this)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode > 0) { //chats need to be updated
            val position = result.resultCode
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            (recyclerView.adapter as ArtistChatsRecyclerAdapter).putItemOfPositionOnTop(position)
        } else if (result.resultCode == -10) {
            refreshChats()
        }
    }

}
