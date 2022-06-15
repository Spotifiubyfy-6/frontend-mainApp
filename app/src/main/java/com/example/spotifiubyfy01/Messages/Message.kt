package com.example.spotifiubyfy01.Messages
import java.io.Serializable
import java.lang.RuntimeException
import java.sql.Time
import java.time.LocalDateTime

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

abstract class MessageItem() {
    abstract fun getMessageType(): MessageEnum
}

class Message(
    var requester_id: Int,
    var receiver_id: Int,
    message: String,
    var time: LocalDateTime
) : Serializable, MessageItem() {
    val messages: ArrayList<String> = ArrayList()
    init{
        messages.add(message)
    }
    
    override fun getMessageType(): MessageEnum {
        if (requester_id == receiver_id)
            return MessageEnum.MESSAGE_RECEIVED
        else
            return MessageEnum.MESSAGE_SENT
    }

    fun addMessageIfSameTime(receiverId: Int, message: String, dateNTime: LocalDateTime): Boolean {
        if ((receiver_id == receiverId) && (time.hour == dateNTime.hour) && (time.minute == dateNTime.minute)) {
            messages.add(message)
            return true
        }
        return false
    }
}
