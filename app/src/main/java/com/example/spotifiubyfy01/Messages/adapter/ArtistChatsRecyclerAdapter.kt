package com.example.spotifiubyfy01.Messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.ChatBundle
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist

class ArtistChatsRecyclerAdapter(
    private val chatsList: List<ChatBundle>,
    private val onClickListener:(ArtistChatViewHolder, ChatBundle, Int) -> Unit
): RecyclerView.Adapter<ArtistChatViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return chatsList[position].getChatType().ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistChatViewHolder {
        return createRespectiveArtistChatHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ArtistChatViewHolder, position: Int) {
        val item = chatsList[position]
        holder.render(item, position, onClickListener)
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }
}