package com.example.spotifiubyfy01.Messages
import java.io.Serializable
import java.sql.Time

data class Message(
    var receiver_id: Int,
    var sender_id: Int,
    var message: String,
    var time: Time?
) : Serializable
