package com.example.spotifiubyfy01.Messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.ChatBundle
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist

class ArtistChatsRecyclerAdapter(
    private val chatsList: List<ChatBundle>,
    private val onClickListener:(Artist) -> Unit
): RecyclerView.Adapter<ArtistChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistChatViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_artist_chat_list_item,
                parent, false)
        return ArtistChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistChatViewHolder, position: Int) {
        val item = chatsList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }
}