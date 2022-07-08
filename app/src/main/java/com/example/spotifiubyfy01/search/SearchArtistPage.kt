package com.example.spotifiubyfy01.search

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.*
import com.example.spotifiubyfy01.Messages.ChatPage
import com.example.spotifiubyfy01.search.adapter.ArtistSearchRecyclerAdapter
import java.io.UnsupportedEncodingException

class SearchArtistPage: BaseActivity(), VolleyCallBack<Artist> {
    var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)
        if(intent.extras?.get("invite") != null) {
            val playlistId = intent.extras?.get("playlist_id")
            initRecyclerViewPlaylistInvite(playlistId as String)
        } else {
            initRecyclerView()
        }
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
                val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
                progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            ArtistSearchRecyclerAdapter(ArrayList()) { artist ->
                onItemClicked(artist)
            }
    }

    private fun initRecyclerViewPlaylistInvite(playlistId: String) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            ArtistSearchRecyclerAdapter(ArrayList()) { artist ->
                onItemClickedInvite(artist, playlistId)
            }
    }

    private fun onItemClickedInvite(artistSearchItem: SearchItem, playlistId: String) {
        val artist = artistSearchItem as Artist
        val artistId = artist.id
        val url = "https://spotifiubyfy-music.herokuapp.com/playlists/$playlistId/collaborators?user_id=$artistId"


        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener {
                Toast.makeText(this, "Colaborator added",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            },
            { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                Log.d(ContentValues.TAG, "ERROR: $errorResponse")
                var body = "undefined error"
                if (errorResponse.networkResponse.data != null) {
                    try {
                        body = String(errorResponse.networkResponse.data, Charsets.UTF_8)
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
                putExtra("popuptext", body)
                putExtra("tokenValidation", true)
            }
                startActivity(intent)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                return params
            }
        }
        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
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
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        progressBar.visibility = View.GONE
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == 0) { //chats need to be updated
            setResult(-10, intent)
            finish()
        }
    }
}
