package com.example.spotifiubyfy01.Messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.Messages.MessageItem

class MessagesRecyclerAdapter(
    private val messageList: List<MessageItem>,
): RecyclerView.Adapter<MessageItemViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return messageList[position].getMessageType().ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {
        return createRespectiveHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: MessageItemViewHolder, position: Int) {
        val item = messageList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}