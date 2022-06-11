package com.example.spotifiubyfy01.Messages

import java.io.Serializable
import java.time.LocalDateTime

data class DateItem(
    val date: LocalDateTime
): Serializable, MessageItem() {
    override fun getMessageType(): MessageEnum {
        return MessageEnum.DATE
    }
}