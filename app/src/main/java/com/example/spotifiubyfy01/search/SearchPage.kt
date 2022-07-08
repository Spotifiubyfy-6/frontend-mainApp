package com.example.spotifiubyfy01.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifiubyfy01.*
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.Playlist
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.search.adapter.SearchRecyclerAdapter

class SearchPage : BaseActivity(), VolleyCallBack<SearchItem> {

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
                searchContainer.visibility = android.view.View.VISIBLE
                DataSource.updateDataSet(this@SearchPage, searchText, this@SearchPage)
                val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
                progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            SearchRecyclerAdapter(ArrayList()) { searchItem ->
                onItemClicked(searchItem)
        }
    }

    private fun onItemClicked(searchItem: SearchItem) {
        when(searchItem.getSearchItemType()) {
            SearchItemEnum.ARTIST_SEARCH_ITEM -> {
                val intent = Intent(this, ArtistPage::class.java)
                intent.putExtra("Artist", searchItem as Artist)
                startActivity(intent)
            }
            SearchItemEnum.ALBUM_SEARCH_ITEM -> {
                val intent = Intent(this, AlbumPage::class.java)
                intent.putExtra("Album", searchItem as Album)
                startActivity(intent)
            }
            SearchItemEnum.PLAYLIST_SEARCH_ITEM -> {
                val intent = Intent(this, PlaylistPage::class.java)
                intent.putExtra("Playlist", searchItem as Playlist)
                startActivity(intent)
            }
            SearchItemEnum.SONG_SEARCH_ITEM -> {
                val app = (this.application as Spotifiubify)
                val song = searchItem as Song

                val userSuscription = app.getProfileData("user_suscription")
                val songSuscription = song.album_suscription

                if ((userSuscription == "free" && (songSuscription == "platinum" || songSuscription == "gold")) ||
                    (userSuscription == "gold" && songSuscription == "platinum")
                ) {
                    Toast.makeText(app, "Change your suscrption to $songSuscription to listen to this song",
                        Toast.LENGTH_LONG).show()
                } else {
                    app.songManager.play(song)
                }
            }
        }
    }

    override fun updateData(list: List<SearchItem>) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = View.GONE
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = recyclerView.adapter as SearchRecyclerAdapter
        adapter.updateList(list)
    }

}