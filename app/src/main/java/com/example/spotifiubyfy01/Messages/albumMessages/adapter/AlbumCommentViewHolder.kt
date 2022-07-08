package com.example.spotifiubyfy01.Messages.albumMessages.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.Messages.albumMessages.Comment
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.Spotifiubify

class AlbumCommentViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val app = ((view.context as AppCompatActivity).application as Spotifiubify)
    private val artistName: TextView = view.findViewById(R.id.artist_name)
    private val image: ImageView = view.findViewById(R.id.artist_image)
    private val comment: TextView = view.findViewById(R.id.comment)
    private val date: TextView = view.findViewById(R.id.date_ago)
    private val authorTag: TextView = view.findViewById(R.id.authorTag)
    private val deleteButton: Button = view.findViewById(R.id.delete_comment)

    fun render(item: Comment, onClickListener: (Comment) -> Unit) {
        val artist = item.artist
        artistName.text = artist.artistName
        val coverRef = app.getStorageReference().child(artist.image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(com.example.spotifiubyfy01.artistProfile.adapter.default_album_image).into(image)
        }
        comment.text = item.comment
        val dateText = "•" + item.getTimeAgo()
        date.text = dateText
        if (item.ownAlbum) {
            deleteButton.visibility = View.VISIBLE

        }

        itemView.setOnClickListener { onClickListener(item) }
        if (item.isAuthor)
            authorTag.visibility = View.VISIBLE
        else
            authorTag.visibility = View.GONE
    }
}