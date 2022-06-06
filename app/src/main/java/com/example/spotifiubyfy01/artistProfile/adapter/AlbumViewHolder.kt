package com.example.spotifiubyfy01.artistProfile.adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify

var default_album_image = "https://ladydanville.files.wordpress.com/2012/03/blankart.png"

class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val albumName: TextView = view.findViewById(R.id.album_name)
    private val albumImage: ImageView = view.findViewById(R.id.album_image)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)

    fun render(album: Album, onClickListener: (Album) -> Unit) {
        albumName.text = album.album_name
        val coverRef = app.getStorageReference().child(album.album_image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(albumImage.context).load(url).into(albumImage)
        }.addOnFailureListener {
            Glide.with(albumImage.context).load(default_album_image).into(albumImage)
        }
        itemView.setOnClickListener { onClickListener(album) }
    }
}