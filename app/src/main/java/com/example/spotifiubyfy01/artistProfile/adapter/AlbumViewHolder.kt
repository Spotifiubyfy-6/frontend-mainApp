package com.example.spotifiubyfy01.ArtistProfile.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.ArtistProfile.Album
import com.example.spotifiubyfy01.R

class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val albumName: TextView = view.findViewById(R.id.album_name)
    private val albumImage: ImageView = view.findViewById(R.id.album_image)

    fun render(album: Album, onClickListener: (Album) -> Unit) {
        albumName.text = album.album_name
        Glide.with(albumImage.context).load(album.album_image).into(albumImage)
        itemView.setOnClickListener { onClickListener(album) }
    }
}