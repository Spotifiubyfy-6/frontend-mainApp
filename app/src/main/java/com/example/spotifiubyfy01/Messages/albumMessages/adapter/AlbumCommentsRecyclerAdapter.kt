package com.example.spotifiubyfy01.Messages.albumMessages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.albumMessages.Comment
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.SearchItem
import com.example.spotifiubyfy01.search.adapter.ArtistViewHolder

class AlbumCommentsRecyclerAdapter(
    private var commentsList: List<Comment>,
    private val onClickListener:(Comment) -> Unit
): RecyclerView.Adapter<AlbumCommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCommentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_album_comment, parent, false)
        return AlbumCommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumCommentViewHolder, position: Int) {
        val item = commentsList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }
}