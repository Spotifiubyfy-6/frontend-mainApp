package com.example.spotifiubyfy01

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.artistProfile.Album
import kotlin.reflect.KFunction2

var default_album_image = "https://i.pinimg.com/originals/33/58/0c/33580cd023504630a4ea63fe0a1650f6.jpg"

class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val playlistName: TextView = view.findViewById(R.id.album_name)
    private val playlistImage: ImageView = view.findViewById(R.id.album_image)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)
    private val deleteButton: Button = view.findViewById<Button>(R.id.delete_album)

    fun render(
        playlist: Playlist,
        position: Int,
        onClickListener: (Playlist) -> Unit,
        onDeleteButtonListener: KFunction2<Playlist, Int, Unit>?
    ) {
        playlistName.text = playlist.playlist_name
        val coverRef = app.getStorageReference().child(playlist.playlist_image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(playlistImage.context).load(url).into(playlistImage)
        }.addOnFailureListener {
            Glide.with(playlistImage.context).load(default_album_image).into(playlistImage)
        }
        if (playlist.forUsersProfile && playlist.type.equals("owned")) {
            deleteButton.visibility = View.VISIBLE
            deleteButton.setOnClickListener {
                if (onDeleteButtonListener != null) {
                    onDeleteButtonListener(playlist, position)
                }
            }
        }
        itemView.setOnClickListener { onClickListener(playlist) }
    }
}