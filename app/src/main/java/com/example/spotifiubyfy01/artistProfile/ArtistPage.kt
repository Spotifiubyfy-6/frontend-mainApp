package com.example.spotifiubyfy01.artistProfile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.*
import com.example.spotifiubyfy01.Messages.ChatPage
import com.example.spotifiubyfy01.artistProfile.adapter.default_album_image
import com.example.spotifiubyfy01.artistProfile.adapter.AlbumRecyclerAdapter
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONObject

class ArtistPage: BaseActivity(), VolleyCallBack<Album> {
    private var artist: Artist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_page)

        val tipBtn = findViewById<Button>(R.id.tip_button)
        val followBtn = findViewById<Button>(R.id.follow_btn)
        val followersText = findViewById<TextView>(R.id.followers)

        artist = intent.extras?.get("Artist") as Artist?
        if (artist == null) { //Artist activity has already been created and is in activities stack
            //savedInstanceState needs to exist
            val artistId = (savedInstanceState?.getString("ArtistID") as String).toInt()
            val artistName = savedInstanceState.getString("ArtistName") as String
            val artistImage = savedInstanceState.getString("ArtistImage") as String  //puede ser aca
            artist = Artist(artistId, artistName, artistImage)

        }

        val artistName = findViewById<TextView>(R.id.artist_name)
        val image = findViewById<ImageView>(R.id.artist_image)
        artistName.text = artist!!.artistName //Use !! because at this point artist is not null
        val coverRef =
            (this.application as Spotifiubify).getStorageReference().child(artist!!.image)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(default_album_image).into(image)
        }
        initAlbumRecyclerView(ArrayList())
        AlbumDataSource.createAlbumList(this, artist!!.id, artist!!.artistName, false, this)

        val messageButton = findViewById<Button>(R.id.message_button)
        messageButton.setOnClickListener {
            val app = (this.application as Spotifiubify)
            if (artist!!.id == (app.getProfileData("id").toString().toInt())) {
                Toast.makeText(
                    this@ArtistPage, "You cannot text yourself!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val chatOnStack = (intent.extras?.get("chat_in_stack") as Boolean?)
            if (chatOnStack == null) {
                val intent = Intent(this, ChatPage::class.java)
                intent.putExtra(
                    "requester_id",
                    (this.application as Spotifiubify).getProfileData("id")!!.toInt()
                )
                intent.putExtra("other", artist!!)
                intent.putExtra("position", 0)
                startActivity(intent)
            } else {
                finish()
            }
        }
        tipBtn.setOnClickListener {
            val intent = Intent(this, TippingPage::class.java).apply {
                putExtra("artist", artist)
            }
            startActivity(intent)
        }

        getFollowings(followBtn)
        getFollowers(followersText)

    }

    private fun follow(followBtn: Button) {
        val url =
            "https://spotifiubyfy-users.herokuapp.com/users/follow/" + artist!!.id.toString()

        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                followBtn.text = "Unfollow"

                followBtn.setOnClickListener {
                    unfollow(followBtn)
                }
            },
            { errorResponse ->
                val intent = Intent(this, PopUpWindow::class.java).apply {
                    val error =
                        errorResponse.networkResponse.data.decodeToString()
                            .split('"')[3]
                    putExtra("popuptext", error)
                }
                startActivity(intent)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(
                    getString(R.string.token_key),
                    Context.MODE_PRIVATE
                ).getString(getString(R.string.token_key), null)
                return params
            }
        }

        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
    }

    private fun unfollow(followBtn: Button) {
        val url =
            "https://spotifiubyfy-users.herokuapp.com/users/unfollow/" + artist!!.id.toString()

        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                followBtn.text = "Follow"
                followBtn.setOnClickListener {
                    follow(followBtn)
                }

            },
            { errorResponse ->
                val intent = Intent(this, PopUpWindow::class.java).apply {
                    val error =
                        errorResponse.networkResponse.data.decodeToString()
                            .split('"')[3]
                    putExtra("popuptext", error)
                }
                startActivity(intent)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(
                    getString(R.string.token_key),
                    Context.MODE_PRIVATE
                ).getString(getString(R.string.token_key), null)
                return params
            }
        }

        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
    }

    private fun getFollowings(followBtn: Button) {
        val userId = (this.application as Spotifiubify).getProfileData("id")
        val url = "https://spotifiubyfy-users.herokuapp.com/users/followings/$userId?skip=0&limit=10"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                for (i in 0 until response.length()) {
                    if ((response.get(i) as JSONObject).getString("following").toInt() == artist?.id) {
                        followBtn.text = "Unfollow"
                    }
                }
                if (followBtn.text == "Unfollow") {
                    followBtn.setOnClickListener {
                        unfollow(followBtn)
                    }
                } else {
                    followBtn.setOnClickListener {
                        follow(followBtn)
                    }
                }
            },
            {
                Toast.makeText(this, "Cant fetch artists right now", Toast.LENGTH_SHORT).show()
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)

    }

    private fun getFollowers(followersText: TextView) {

        val artistId = artist?.id.toString()
        val url = "https://spotifiubyfy-users.herokuapp.com/users/followers/$artistId?skip=0&limit=10"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->   followersText.text = "${response.length()} Followers"

            },
            {
                Toast.makeText(this, "Cant fetch followers right now", Toast.LENGTH_SHORT).show()
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun initAlbumRecyclerView(albumList: List<Album>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                                    false)
        recyclerView.adapter =
            AlbumRecyclerAdapter(albumList as MutableList<Album>, { album ->
                onItemClicked(album)
            }, null)
    }

    override fun updateData(list: List<Album>) {
        initAlbumRecyclerView(list)
    }

    private fun onItemClicked(album: Album) {
        val intent = Intent(this, AlbumPage::class.java)
        intent.putExtra("Album", album)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("ArtistId", artist!!.id.toString())
        outState.putString("ArtistName", artist!!.artistName)
        outState.putString("ArtistImage", artist!!.image)
        super.onSaveInstanceState(outState)
    }

}
