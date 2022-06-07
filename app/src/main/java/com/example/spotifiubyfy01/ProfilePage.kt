package com.example.spotifiubyfy01

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.AlbumDataSource
import com.example.spotifiubyfy01.artistProfile.AlbumDataSource.Companion.createAlbumList
import com.example.spotifiubyfy01.search.VolleyCallBack


class ProfilePage : AppCompatActivity(), VolleyCallBack<Album> {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_playback) {
            startActivity(Intent(this, ReproductionPage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val app = (this.application as Spotifiubify)

        findViewById<TextView>(R.id.username).text = app.getProfileData("username")
        findViewById<TextView>(R.id.email).text = app.getProfileData("email")
        findViewById<TextView>(R.id.subscription).text = app.getProfileData("user_suscription")

        if ( !intent.getStringExtra("passwordChangeSuccess").isNullOrEmpty()) {
            Toast.makeText(this, intent.getStringExtra("passwordChangeSuccess"),Toast.LENGTH_LONG).show()
        }
        val logOutClick = findViewById<Button>(R.id.logOutButton)
        logOutClick.setOnClickListener {
            val sharedPref = getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(getString(R.string.token_key), "")
                apply()
            }
            val intent = Intent(this, MainLandingPage::class.java)
            startActivity(intent)
        }

        val editClick = findViewById<Button>(R.id.editButton)
        editClick.setOnClickListener {
            val intent = Intent(this, ProfileEditPage::class.java)
            startActivity(intent)
            finish()
        }

        val createAlbumClick = findViewById<Button>(R.id.createAlbumButton)
        createAlbumClick.setOnClickListener {
            val intent = Intent(this, AlbumCreationPage::class.java)
            startActivity(intent)
        }

        val createSongButton = findViewById<Button>(R.id.createSongButton)
        createSongButton.setOnClickListener {
            createAlbumList(this, app.getProfileData("id")!!.toInt(),
                app.getProfileData("username")!!, this)
        }
	
	val tempClick = findViewById<Button>(R.id.createPlaylistButton)
        tempClick.setOnClickListener {
            val intent = Intent(this, PlaylistCreationPage::class.java)
            startActivity(intent)
        }
    }

    override fun updateData(list: List<Album>) {
        if (list.isEmpty()) {
            Toast.makeText(this, "You do not have any albums. Create an album first.",
                Toast.LENGTH_LONG).show()
            return
        }
        val intent = Intent(this, SongCreationPage::class.java)
        intent.putExtra("albums", ArrayList(list))
        startActivity(intent)
    }
}
