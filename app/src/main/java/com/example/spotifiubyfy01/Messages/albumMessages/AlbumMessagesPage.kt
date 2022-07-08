package com.example.spotifiubyfy01.Messages.albumMessages

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.*
import com.example.spotifiubyfy01.Messages.albumMessages.adapter.AlbumCommentsRecyclerAdapter
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.w3c.dom.Text

class AlbumMessagesPage : BaseActivity(), VolleyCallBack<Comment> {
    private var ownAlbum: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_comments)
        val app = (this.application as Spotifiubify)
        val albumId = (intent.extras!!.get("albumId") as String).toInt()
        val authorId = (intent.extras!!.get("authorId") as String).toInt()
        val myId = (app.getProfileData("id") as String).toInt()
        val aux = intent.extras?.get("ownAlbum") as Boolean?
        if (aux != null)
            ownAlbum = true
        CommentsDataSource.getCommentsOfAlbum(this, albumId, authorId, myId, ownAlbum, this)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = View.VISIBLE
        val commentTextBox = findViewById<EditText>(R.id.comment_text)
        val myArtist = Artist(myId, (app.getProfileData("name") as String),
                "profilePictures/"+app.getProfileData("username").toString())
        val sendButton = findViewById<Button>(R.id.comment_button)

        sendButton.setOnClickListener{
            CommentsDataSender.makeComment(this, myArtist, albumId, commentTextBox.text.toString(),
                                            this::addComment, authorId == myId)
            commentTextBox.text.clear()
        }
    }

    private fun initRecyclerView(commentsList: ArrayList<Comment>) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = View.GONE
        if (commentsList.isEmpty()) {
            val infoText = findViewById<TextView>(R.id.informationText)
            infoText.visibility = View.VISIBLE
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AlbumCommentsRecyclerAdapter(commentsList, this::onItemClicked,
                                                        this::onDeleteButtonClicked)
    }

    private fun onDeleteButtonClicked(comment: Comment, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Do you want to delete this comment?")
        alertDialogBuilder.setMessage("This action is irreversible.")
        alertDialogBuilder.setNegativeButton("yes") { _, _ ->
            DeleteSender.deleteComment(this, comment.id, comment.artist.id, position, this::onCommentDeletion)
        }
        alertDialogBuilder.setPositiveButton("no", null)
        alertDialogBuilder.show()
    }

    private fun onCommentDeletion(position: Int) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        (recyclerView.adapter as AlbumCommentsRecyclerAdapter).deleteItemOfPosition(position)
    }

    private fun onItemClicked(comment: Comment) {
        val intent = Intent(this, ArtistPage::class.java)
        intent.putExtra("Artist", comment.artist)
        startActivity(intent)
    }

    private fun addComment(comment: Comment) {
        val infoText = findViewById<TextView>(R.id.informationText)
        infoText.visibility = View.GONE
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter =(recyclerView.adapter as AlbumCommentsRecyclerAdapter)
        adapter.addComment(comment)
        recyclerView.smoothScrollToPosition(0)
    }

    override fun updateData(list: List<Comment>) {
        val arrayList = ArrayList<Comment>(list)
        initRecyclerView(arrayList)
    }

}