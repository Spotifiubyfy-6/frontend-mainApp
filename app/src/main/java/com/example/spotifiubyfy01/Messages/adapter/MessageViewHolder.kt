package com.example.spotifiubyfy01.Messages.adapter

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val messageBox: TextView = view.findViewById(R.id.message)

    fun render(message: Message) {
        messageBox.setText(message.message)
    }
}