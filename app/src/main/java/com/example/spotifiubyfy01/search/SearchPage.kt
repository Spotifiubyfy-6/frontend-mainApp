package com.example.spotifiubyfy01.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.R

class SearchPage : AppCompatActivity() {

    private lateinit var artistAdapter : ArtistRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)
        val search = findViewById<EditText>(R.id.search_textfield)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val searchContainer =
                    findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
                if (search.text.toString().isEmpty()) {
                    searchContainer.visibility = android.view.View.GONE
                } else {
                    searchContainer.visibility = android.view.View.VISIBLE
                }
            }
        })
        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet() {
        val data = DataSource.createDataSet()
        artistAdapter.submitList(data)
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchPage)
            artistAdapter = ArtistRecyclerAdapter()
            adapter = artistAdapter
        }
    }
}