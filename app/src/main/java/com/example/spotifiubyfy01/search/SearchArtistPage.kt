package com.example.spotifiubyfy01.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.Messages.ChatPage
import com.example.spotifiubyfy01.Messages.adapter.ArtistChatsRecyclerAdapter
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.ReproductionPage
import com.example.spotifiubyfy01.Spotifiubify
import com.example.spotifiubyfy01.search.adapter.ArtistSearchRecyclerAdapter

class SearchArtistPage: AppCompatActivity(), VolleyCallBack<Artist> {
    var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)
        initRecyclerView()
        val app = (this.application as Spotifiubify)
        userId = app.getProfileData("id")!!.toInt()
        val search = findViewById<EditText>(R.id.search_textfield)
        search.hint = "Search artists to message..."
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val searchContainer =
                    findViewById<RecyclerView>(R.id.recycler_view)
                val searchText = search.text.toString()
                if (searchText.isEmpty()) {
                    searchContainer.visibility = android.view.View.GONE
                    return
                }
                searchContainer.visibility = android.view.View.VISIBLE
                DataSource.updateDataSetOfArtist(this@SearchArtistPage, searchText, this@SearchArtistPage)
            }
        })
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            ArtistSearchRecyclerAdapter(ArrayList()) { artist ->
                onItemClicked(artist as SearchItem)
            }
    }

    private fun onItemClicked(artistSearchItem: SearchItem) {
        val artist = artistSearchItem as Artist
        if (userId!! == artist.id) {
            Toast.makeText(this, "You cannot text yourself!",
                Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, ChatPage::class.java)
        intent.putExtra("requester_id", userId!!)
        intent.putExtra("other", artist)
        intent.putExtra("position", 0)
        resultLauncher.launch(intent)
    }

    override fun updateData(list: List<Artist>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = recyclerView.adapter as ArtistSearchRecyclerAdapter
        adapter.updateList(list)
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

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == 0) { //chats need to be updated
            setResult(-10, intent)
            finish()
        }
    }
}
