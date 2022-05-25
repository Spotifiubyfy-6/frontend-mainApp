package com.example.spotifiubyfy01.search

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.R
import com.example.spotifiubyfy01.search.adapter.ArtistRecyclerAdapter

class SearchPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)
        val searchContainer =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
        searchContainer.visibility = android.view.View.GONE
        val search = findViewById<EditText>(R.id.search_textfield)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val searchContainer =
                    findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
                Log.d(TAG, search.text.toString())
                if (search.text.toString().isEmpty()) {
                    searchContainer.visibility = android.view.View.GONE
                } else {
                    searchContainer.visibility = android.view.View.VISIBLE
                }
            }
        })
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ArtistRecyclerAdapter(DataSource.createDataSet()) {artist ->
                onItemClicked(artist)
        }
    }

    fun onItemClicked(artist: Artist) {
        Toast.makeText(this, artist.username, Toast.LENGTH_SHORT).show()
    }
}