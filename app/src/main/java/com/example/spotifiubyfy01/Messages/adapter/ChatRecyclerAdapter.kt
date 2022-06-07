package com.example.spotifiubyfy01.Messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist

class MessagesRecyclerAdapter(
    private val messageList: List<Message>,
): RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_message_item,
                parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messageList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}