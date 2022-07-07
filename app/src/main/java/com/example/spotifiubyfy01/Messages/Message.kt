package com.example.spotifiubyfy01.Messages
import android.util.Log
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun convertIntToMessageEnum(value: Int): MessageEnum {
    return when(value) {
        0 -> MessageEnum.MESSAGE_RECEIVED
        1 -> MessageEnum.MESSAGE_SENT
        2 -> MessageEnum.DATE
        else -> throw RuntimeException() //should never happen
    }
}

enum class MessageEnum {
    MESSAGE_RECEIVED, MESSAGE_SENT, DATE; //NEVER CHANGE THIS ORDER
}

abstract class MessageItem {
    abstract fun getMessageType(): MessageEnum
}

class Message(
    private var requester_id: Int,
    var receiver_id: Int,
    message: String,
    val time: ZonedDateTime
) : Serializable, MessageItem() {
    val messages: ArrayList<String> = ArrayList()
    init{
        messages.add(message)
    }
    
    override fun getMessageType(): MessageEnum {
        return if (requester_id == receiver_id)
            MessageEnum.MESSAGE_RECEIVED
        else
            MessageEnum.MESSAGE_SENT
    }

    fun addMessageIfSameTimeNReceiver(receiverId: Int, message: String, dateNTime: ZonedDateTime): Boolean {
        if ((receiver_id == receiverId) && (time.hour == dateNTime.hour) && (time.minute == dateNTime.minute)) {
            messages.add(message)
            return true
        }
        return false
    }
}
