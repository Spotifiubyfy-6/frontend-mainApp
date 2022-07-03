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
import com.example.spotifiubyfy01.Messages.ChatBundle
import com.example.spotifiubyfy01.Messages.ChatBundleEnum
import com.example.spotifiubyfy01.Messages.convertIntToChatBundleEnum
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify

fun createRespectiveArtistChatHolder(parent: ViewGroup, viewType: Int) : ArtistChatViewHolder {
    val holder =
        when (convertIntToChatBundleEnum(viewType)) {
            ChatBundleEnum.CHAT_NOT_SEEN ->    {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_artist_chat_list_item, parent, false)
                ArtistChatNotSeenViewHolder(view)
            }
            ChatBundleEnum.CHAT_SEEN -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_artist_chat_list_item, parent, false)
                ArtistChatSeenViewHolder(view)
            }
        }
    return holder
}

abstract class ArtistChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun render(chatBundle: ChatBundle, position: Int,
                        onClickListener: (ArtistChatViewHolder, ChatBundle, Int) -> Unit)

    abstract fun changeToSeen()
}

class ArtistChatSeenViewHolder(view: View) : ArtistChatViewHolder(view) {
    private val artistName: TextView = view.findViewById(R.id.artist_name)
    private val image: ImageView = view.findViewById(R.id.artist_image)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)

    override fun render(chatBundle: ChatBundle, position: Int,
                        onClickListener: (ArtistChatViewHolder, ChatBundle, Int) -> Unit) {
        artistName.text = chatBundle.artist.artistName
        val coverRef = app.getStorageReference().child(chatBundle.artist.image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(com.example.spotifiubyfy01.artistProfile.adapter.default_album_image).into(image)
        }
        itemView.setOnClickListener { onClickListener(this, chatBundle, position) }
    }

    override fun changeToSeen() {
        //No need to change
    }
}

class ArtistChatNotSeenViewHolder(view: View) : ArtistChatViewHolder(view) {
    private val artistName: TextView = view.findViewById(R.id.artist_name)
    private val image: ImageView = view.findViewById(R.id.artist_image)
    private val notSeenBox: TextView = view.findViewById(R.id.number_of_not_seen_messages)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)

    override fun render(chatBundle: ChatBundle, position: Int,
                        onClickListener: (ArtistChatViewHolder, ChatBundle, Int) -> Unit) {
        artistName.text = chatBundle.artist.artistName
        val coverRef = app.getStorageReference().child(chatBundle.artist.image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(com.example.spotifiubyfy01.artistProfile.adapter.default_album_image).into(image)
        }
        itemView.setOnClickListener { onClickListener(this, chatBundle, position) }
        artistName.setTypeface(null, Typeface.BOLD)
        notSeenBox.visibility = View.VISIBLE
        notSeenBox.text = chatBundle.number_of_not_seen.toString()
    }

    override fun changeToSeen() {
        artistName.setTypeface(null, Typeface.NORMAL)
        notSeenBox.visibility = View.INVISIBLE
    }
}
