package com.example.spotifiubyfy01.search

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.AlbumPage
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.search.adapter.SearchRecyclerAdapter

class SearchPage : AppCompatActivity(), VolleyCallBack<SearchItem> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)
        initRecyclerView()
        val search = findViewById<EditText>(R.id.search_textfield)
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
                DataSource.updateDataSet(this@SearchPage, searchText, this@SearchPage)
            }
        })
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            SearchRecyclerAdapter(ArrayList()) { artist ->
                onItemClicked(artist)
        }
    }

    private fun onItemClicked(searchItem: SearchItem) {
        val intent = when(searchItem.getSearchItemType()) {
            ARTIST_SEARCH_ITEM -> {
                val intent = Intent(this, ArtistPage::class.java)
                intent.putExtra("Artist", searchItem as Artist)
            }
            ALBUM_SEARCH_ITEM -> {
                val intent = Intent(this, AlbumPage::class.java)
                intent.putExtra("Album", searchItem as Album)
            }
            else -> {
                val intent = Intent(this, ArtistPage::class.java)
                intent.putExtra("Artist", searchItem as Artist)
            }
        }
        startActivity(intent)
    }

    override fun updateDataInRecyclerView(list: List<SearchItem>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = recyclerView.adapter as SearchRecyclerAdapter
        adapter.updateList(list)
        recyclerView.visibility = android.view.View.VISIBLE
    }

}