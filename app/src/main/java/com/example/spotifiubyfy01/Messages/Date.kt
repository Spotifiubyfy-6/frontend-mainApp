package com.example.spotifiubyfy01.Messages

import java.io.Serializable

data class DateItem(
    val date: String
): Serializable, MessageItem() {
    override fun getMessageType(): MessageEnum {
        return MessageEnum.DATE
    }
}