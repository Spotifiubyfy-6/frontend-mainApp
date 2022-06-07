package com.example.spotifiubyfy01.Messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.artistProfile.adapter.AlbumViewHolder
import com.example.spotifiubyfy01.search.Artist

class MessagesRecyclerAdapter(
    private val chatsList: List<Artist>,
    private val onClickListener:(Artist) -> Unit
): RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_artist_chat_list_item,
                parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = chatsList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }
}