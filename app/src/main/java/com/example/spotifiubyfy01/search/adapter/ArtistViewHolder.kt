package com.example.spotifiubyfy01.search.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchItem

class ArtistViewHolder(view: View) : SearchViewHolder(view) {

    private val artistName: TextView = view.findViewById(R.id.artist_name)
    private val image: ImageView = view.findViewById(R.id.artist_image)
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)

    override fun render(item: SearchItem, onClickListener: (SearchItem) -> Unit) {
        val artist = item as Artist
        artistName.text = artist.artistName
        val coverRef = app.getStorageReference().child(artist.image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(com.example.spotifiubyfy01.artistProfile.adapter.default_album_image).into(image)
        }
        itemView.setOnClickListener { onClickListener(artist) }
    }
}
