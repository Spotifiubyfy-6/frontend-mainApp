package com.example.spotifiubyfy01.Messages

import com.example.spotifiubyfy01.search.Artist
import java.io.Serializable
import java.lang.RuntimeException

fun convertIntToChatBundleEnum(value: Int): ChatBundleEnum {
    return when(value) {
        0 -> ChatBundleEnum.CHAT_NOT_SEEN
        1 -> ChatBundleEnum.CHAT_SEEN
        else -> throw RuntimeException() //should never happen
    }
}

enum class ChatBundleEnum {
    CHAT_NOT_SEEN, CHAT_SEEN; //NEVER CHANGE THIS ORDER
}

data class ChatBundle(
    var artist: Artist,
    var chat_seen: Boolean): Serializable {
    fun getChatType(): ChatBundleEnum {
        return if (chat_seen)
            ChatBundleEnum.CHAT_SEEN
        else
            ChatBundleEnum.CHAT_NOT_SEEN
    }
}
