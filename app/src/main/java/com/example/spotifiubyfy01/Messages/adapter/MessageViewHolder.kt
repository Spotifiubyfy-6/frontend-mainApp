package com.example.spotifiubyfy01.Messages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.Messages.MessageEnum
import com.example.spotifiubyfy01.Messages.MessageItem
import com.example.spotifiubyfy01.Messages.convertIntToMessageEnum
import com.example.spotifiubyfy01.R

fun createRespectiveHolder(parent: ViewGroup, viewType: Int) : MessageItemViewHolder {
    val holder =
        when (convertIntToMessageEnum(viewType)) {
            MessageEnum.MESSAGE_RECEIVED ->    {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_message_received_item, parent, false)
                MessageViewHolder(view)
            }
            MessageEnum.MESSAGE_SENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_message_sent_item, parent, false)
                MessageViewHolder(view)
            }
        }
    return holder
}

abstract class MessageItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun render(message: MessageItem)
}

class MessageViewHolder(view: View) : MessageItemViewHolder(view) {
    private val messageBox: TextView = view.findViewById(R.id.message)

    override fun render(message: MessageItem) {
        messageBox.text = (message as Message).message
    }
}
