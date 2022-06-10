package com.example.spotifiubyfy01.Messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.DateItem
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.Messages.MessageItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MessagesRecyclerAdapter(
    private val messageList: ArrayList<MessageItem>,
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

    fun addMessage(message: Message, dateNTime: LocalDateTime) {
        if (messageList.size == 0) {
            val date =  dateNTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toString()
            messageList.add(DateItem(date))
        } else { //there were messages before and last item is always a message
            val lastMessageDateNTime = (messageList.last() as Message).time
            val lastMessageDay = LocalDate.of(lastMessageDateNTime.year, lastMessageDateNTime.month,
                                                lastMessageDateNTime.dayOfMonth)
            val currentDay = LocalDate.of(dateNTime.year, dateNTime.month, dateNTime.dayOfMonth)
            if (currentDay > lastMessageDay) {
                val date =  dateNTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toString()
                messageList.add(DateItem(date))
            }
        }
        messageList.add(message)
        this.notifyItemInserted(messageList.size - 1)
    }
}