package com.example.spotifiubyfy01.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.adapter.default_album_image
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchItem

var default_album_image = "https://ladydanville.files.wordpress.com/2012/03/blankart.png"

class AlbumSearchViewHolder(view: View) : SearchViewHolder(view) {

    private val albumName: TextView = view.findViewById(R.id.album_name)
    private val artistName: TextView = view.findViewById(R.id.album_specification)
    private val albumImage: ImageView = view.findViewById(R.id.album_image)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)

    override fun render(item: SearchItem, onClickListener: (SearchItem) -> Unit) {
        val album = item as Album
        albumName.text = album.album_name
        val text = "Album by " + album.artist_name
        artistName.text = text
        val coverRef = app.getStorageReference().child(album.album_image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(albumImage.context).load(url).into(albumImage)
        }.addOnFailureListener {
            Glide.with(albumImage.context).load(default_album_image).into(albumImage)
        }
        itemView.setOnClickListener { onClickListener(album) }
    }
}