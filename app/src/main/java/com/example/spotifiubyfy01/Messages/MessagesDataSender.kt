package com.example.spotifiubyfy01.Messages

import android.util.Log

class MessagesDataSender {

    companion object {
        fun sendMessage(message: String) {
            if (message.isEmpty())
                return
            Log.d("TAG", message)
        }
    }
}