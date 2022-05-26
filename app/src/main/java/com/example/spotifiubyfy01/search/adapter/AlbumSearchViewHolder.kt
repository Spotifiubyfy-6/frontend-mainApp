package com.example.spotifiubyfy01.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchItem

class AlbumSearchViewHolder(view: View) : SearchViewHolder(view) {

    private val albumName: TextView = view.findViewById(R.id.album_name)
    private val artistName: TextView = view.findViewById(R.id.album_specification)
    private val albumImage: ImageView = view.findViewById(R.id.album_image)

    override fun render(item: SearchItem, onClickListener: (SearchItem) -> Unit) {
        val album = item as Album
        albumName.text = album.album_name
        val text = "Album by" + album.artist_name
        artistName.text = text
        Glide.with(albumImage.context).load(album.album_image).into(albumImage)
        itemView.setOnClickListener { onClickListener(album) }
    }
}