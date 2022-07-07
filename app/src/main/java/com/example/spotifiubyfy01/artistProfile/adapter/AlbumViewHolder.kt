package com.example.spotifiubyfy01.artistProfile.adapter

import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.artistProfile.Album
import kotlin.reflect.KFunction2

var default_album_image = "https://i.pinimg.com/originals/33/58/0c/33580cd023504630a4ea63fe0a1650f6.jpg"

class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val albumName: TextView = view.findViewById(R.id.album_name)
    private val albumImage: ImageView = view.findViewById(R.id.album_image)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)
    private val deleteButton: Button = view.findViewById<Button>(R.id.delete_album)
    private val context = view.context

    fun render(
        album: Album,
        position: Int,
        onClickListener: (Album) -> Unit,
        onDeleteButtonListener: KFunction2<Album, Int, Unit>?
    ) {
        albumName.text = album.album_name
        val coverRef = app.getStorageReference().child(album.album_image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(albumImage.context).load(url).into(albumImage)
        }.addOnFailureListener {
            Glide.with(albumImage.context).load(default_album_image).into(albumImage)
        }
        if (album.forUsersProfile) {
            deleteButton.visibility = VISIBLE
            deleteButton.setOnClickListener {
                if (onDeleteButtonListener != null) {
                    onDeleteButtonListener(album, position)
                }
            }
        }
        itemView.setOnClickListener { onClickListener(album) }
    }


}