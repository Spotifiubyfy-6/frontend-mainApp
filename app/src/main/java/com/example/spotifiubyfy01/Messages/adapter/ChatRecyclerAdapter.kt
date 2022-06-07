package com.example.spotifiubyfy01.Messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.Message

class MessagesRecyclerAdapter(
    private val messageList: List<Message>,
): RecyclerView.Adapter<MessageViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return messageList[position].getMessageType().ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return createRespectiveHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messageList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}