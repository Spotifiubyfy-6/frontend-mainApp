package com.example.spotifiubyfy01.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchItem

class ArtistViewHolder(view: View) : SearchViewHolder(view) {

    private val artistName: TextView = view.findViewById(R.id.artist_name)
    private val image: ImageView = view.findViewById(R.id.artist_image)

    override fun render(item: SearchItem, onClickListener: (SearchItem) -> Unit) {
        val artist = item as Artist
        artistName.text = artist.username
        Glide.with(image.context).load(artist.image).into(image)
        itemView.setOnClickListener { onClickListener(artist) }
    }
}
