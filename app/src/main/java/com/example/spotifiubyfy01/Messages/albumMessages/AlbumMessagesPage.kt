package com.example.spotifiubyfy01.Messages.albumMessages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.albumMessages.adapter.AlbumCommentsRecyclerAdapter
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.ReproductionPage
import com.example.spotifiubyfy01.search.VolleyCallBack

class AlbumMessagesPage : AppCompatActivity(), VolleyCallBack<Comment> {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_comments)
        val albumId = (intent.extras!!.get("albumId") as String).toInt()
        initRecyclerView(ArrayList())
        CommentsDataSource.getCommentsOfAlbum(this, albumId, this)
    }

    private fun initRecyclerView(commentsList: List<Comment>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AlbumCommentsRecyclerAdapter(commentsList) { comment: Comment ->
            onItemClicked(comment)
        }
    }

    private fun onItemClicked(comment: Comment) {
        Log.d("TAG", "Comment clicked!")
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

    override fun updateData(list: List<Comment>) {
        initRecyclerView(list)
    }
}