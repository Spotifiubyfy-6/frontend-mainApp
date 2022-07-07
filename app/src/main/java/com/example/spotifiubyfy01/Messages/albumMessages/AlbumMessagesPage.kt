package com.example.spotifiubyfy01.Messages.albumMessages

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.albumMessages.adapter.AlbumCommentsRecyclerAdapter
import com.example.spotifiubyfy01.BaseActivity
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.ReproductionPage
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack

class AlbumMessagesPage : BaseActivity(), VolleyCallBack<Comment> {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_comments)
        val app = (this.application as Spotifiubify)
        val albumId = (intent.extras!!.get("albumId") as String).toInt()
        val authorId = (intent.extras!!.get("authorId") as String).toInt()
        initRecyclerView(ArrayList())
        CommentsDataSource.getCommentsOfAlbum(this, albumId, authorId, this)

        val commentTextBox = findViewById<EditText>(R.id.comment_text)
        val myId = (app.getProfileData("id") as String).toInt()
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
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AlbumCommentsRecyclerAdapter(commentsList) { comment: Comment ->
            onItemClicked(comment)
        }
    }

    private fun onItemClicked(comment: Comment) {
        val intent = Intent(this, ArtistPage::class.java)
        intent.putExtra("Artist", comment.artist)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_playback) {
            startActivity(Intent(this, ReproductionPage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addComment(comment: Comment) {
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