package com.example.spotifiubyfy01.Messages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.Messages.MessageEnum
import com.example.spotifiubyfy01.Messages.convertIntToMessageEnum
import com.example.spotifiubyfy01.R

fun createRespectiveHolder(parent: ViewGroup, viewType: Int) : MessageViewHolder {
    val holder =
        when (convertIntToMessageEnum(viewType)) {
            MessageEnum.MESSAGE_RECEIVED ->    {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_message_received_item, parent, false)
                MessageReceivedViewHolder(view)
            }
            MessageEnum.MESSAGE_SENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_message_received_item, parent, false)
                MessageReceivedViewHolder(view)
            }
        }
    return holder
}

abstract class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun render(message: Message)
}

class MessageReceivedViewHolder(view: View) : MessageViewHolder(view) {

    private val messageBox: TextView = view.findViewById(R.id.message)

    override fun render(message: Message) {
        messageBox.setText(message.message)
    }
}