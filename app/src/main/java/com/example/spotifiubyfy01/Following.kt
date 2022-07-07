package com.example.spotifiubyfy01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.adapter.SearchRecyclerAdapter
import org.json.JSONArray
import org.json.JSONObject

class Following : NotificationReceiverActivity() {

    private var followings = mutableListOf<Artist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)

        getFollowings()
    }


    private fun initRecyclerViewArtist(listOfArtist:  List<Artist>) {
        val recyclerViewArtist= findViewById<RecyclerView>(R.id.recycler_view_artist_following)
        recyclerViewArtist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
            false)
        recyclerViewArtist.adapter = SearchRecyclerAdapter(listOfArtist) { artist ->
            onArtistClicked(artist as Artist)
        }
    }

    private fun onArtistClicked(artist: Artist) {
        val intent = Intent(this, ArtistPage::class.java)
        intent.putExtra("Artist", artist)
        startActivity(intent)
    }

    private fun getFollowings() {
        val userId = (this.application as Spotifiubify).getProfileData("id")
        val url = "https://spotifiubyfy-users.herokuapp.com/users/followings/$userId?skip=0&limit=10"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->

                for (i in 0 until response.length()) {
                    followings.add(getArtist(response.get(i) as JSONObject))
                }
                initRecyclerViewArtist(followings)
            },
            {
                Toast.makeText(this, "Cant fetch artists right now", Toast.LENGTH_SHORT).show()
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)

    }



    private fun getArtist(jsonArtist : JSONObject): Artist {
        val id = jsonArtist.getString("following").toInt()
        val username = jsonArtist.getString("name")
        val artistImage = "profilePictures/"+jsonArtist.getString("username").toString()
        return Artist(id, username  , artistImage)
    }


}