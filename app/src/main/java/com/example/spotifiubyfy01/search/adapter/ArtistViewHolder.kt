package com.example.spotifiubyfy01.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist

class ArtistViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val artist_name = view.findViewById<TextView>(R.id.artist_name)
    val image = view.findViewById<ImageView>(R.id.artist_image)

    fun render(artist: Artist, onClickListener: (Artist) -> Unit) {
        artist_name.text = artist.username
        Glide.with(image.context).load(artist.image).into(image)
        itemView.setOnClickListener { onClickListener(artist) }
    }
}
