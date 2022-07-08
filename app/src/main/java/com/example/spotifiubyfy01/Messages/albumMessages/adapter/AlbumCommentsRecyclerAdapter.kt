package com.example.spotifiubyfy01.Messages.albumMessages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.albumMessages.Comment
import com.example.spotifiubyfy01.R

class AlbumCommentsRecyclerAdapter(
    private val commentsList: MutableList<Comment>,
    private val onClickListener: (Comment) -> Unit,
    private val onDeleteButton: (Comment, Int) -> Unit
): RecyclerView.Adapter<AlbumCommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCommentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_album_comment, parent, false)
        return AlbumCommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumCommentViewHolder, position: Int) {
        val item = commentsList[position]
        holder.render(item, position, onClickListener, onDeleteButton)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    fun addComment(comment: Comment) {
        commentsList.add(0, comment)
        this.notifyItemInserted(0)
    }

    fun deleteItemOfPosition(position: Int) {
        commentsList.removeAt(position)
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, commentsList.size)
    }
}