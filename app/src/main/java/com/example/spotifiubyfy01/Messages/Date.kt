package com.example.spotifiubyfy01.Messages

import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class DateItem(
    val date: ZonedDateTime
): Serializable, MessageItem() {
    override fun getMessageType(): MessageEnum {
        return MessageEnum.DATE
    }
}