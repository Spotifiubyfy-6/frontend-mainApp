package com.example.spotifiubyfy01.Messages.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.Messages.*
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchItem

fun createRespectiveArtistChatHolder(parent: ViewGroup, viewType: Int) : ArtistChatViewHolder {
    val holder =
        when (convertIntToChatBundleEnum(viewType)) {
            ChatBundleEnum.CHAT_NOT_SEEN ->    {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_artist_chat_list_item, parent, false)
                val artistName = view.findViewById<TextView>(R.id.artist_name)
                artistName.setTypeface(null, Typeface.BOLD)
                ArtistChatViewHolder(view)
            }
            ChatBundleEnum.CHAT_SEEN -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_artist_chat_list_item, parent, false)
                ArtistChatViewHolder(view)
            }
        }
    return holder
}


class ArtistChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val artistName: TextView = view.findViewById(R.id.artist_name)
    private val image: ImageView = view.findViewById(R.id.artist_image)

    fun render(chatBundle: ChatBundle, onClickListener: (ChatBundle) -> Unit) {
        artistName.text = chatBundle.artist.username
        Glide.with(image.context).load(chatBundle.artist.image).into(image)
        itemView.setOnClickListener { onClickListener(chatBundle) }
    }
}