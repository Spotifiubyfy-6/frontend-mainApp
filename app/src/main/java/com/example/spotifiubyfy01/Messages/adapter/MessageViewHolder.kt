package com.example.spotifiubyfy01.Messages.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.*
import com.example.spotifiubyfy01.R
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
            MessageEnum.DATE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_chat_date, parent, false)
                DateViewHolder(view)
            }
        }
    return holder
}

abstract class MessageItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun render(messageItem: MessageItem)
}

class MessageViewHolder(view: View) : MessageItemViewHolder(view) {
    private val messageBox: TextView = view.findViewById(R.id.message)
    private val hour: TextView = view.findViewById(R.id.hour)
    override fun render(messageItem: MessageItem) {
        val message = messageItem as Message
        var text = message.messages[0]
        for (i in 1 until message.messages.size)
            text = text + '\n' + message.messages[i]
        Log.d("TAG", text)
        messageBox.text = text
        hour.text = message.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    }
}

class DateViewHolder(view: View) : MessageItemViewHolder(view) {
    private val dateBox: TextView = view.findViewById(R.id.date)
    override fun render(messageItem: MessageItem) {
        dateBox.text = (messageItem as DateItem).date
            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toString()
    }
}
