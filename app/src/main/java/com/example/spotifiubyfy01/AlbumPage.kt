package com.example.spotifiubyfy01

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.Messages.albumMessages.AlbumMessagesPage
import com.example.spotifiubyfy01.artistProfile.Album
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.artistProfile.adapter.SongRecyclerAdapter
import com.example.spotifiubyfy01.artistProfile.adapter.default_album_image

class AlbumPage : BaseActivity() {

    private var itemDeleted: Boolean = false
    private var ownAlbum: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val app = (this.application as Spotifiubify)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_page)
        val album = intent.extras?.get("Album") as Album
        ownAlbum = intent.extras?.get("ownAlbum") as Boolean?
        findViewById<TextView>(R.id.albumName).text = album.album_name
        findViewById<TextView>(R.id.artistName).text = album.artist_name
        findViewById<TextView>(R.id.album_genre).text = album.album_genre
        findViewById<TextView>(R.id.album_description).text = album.album_description
        findViewById<TextView>(R.id.album_suscription).text = album.album_suscription

        val editButton = findViewById<Button>(R.id.editAlbum)
        if (ownAlbum != null) {
            editButton.visibility = VISIBLE
            editButton.setOnClickListener {
                val intent = Intent(this, AlbumCreationPage::class.java)
                intent.putExtra("id", album.album_id)
                intent.putExtra("name", album.album_name)
                intent.putExtra("description", album.album_description)
                intent.putExtra("genre", album.album_genre)
                intent.putExtra("suscription", album.album_suscription)
                intent.putExtra("image", album.album_image)
                startActivity(intent)
            }
        }

        val image = findViewById<ImageView>(R.id.album_image)


        val coverRef = app.getStorageReference().child(album.album_image)

        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(image.context).load(url).into(image)
        }.addOnFailureListener {
            Glide.with(image.context).load(default_album_image).into(image)
        }

        initRecyclerView(album.song_list)
        val playButton = findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener {
            //play album! obtain album songs using album.song_list


            val userSuscription = app.getProfileData("user_suscription")
            val albumSuscription = album.album_suscription
            if ((userSuscription == "free" && (albumSuscription == "platinum" || albumSuscription == "gold")) ||
                (userSuscription == "gold" && albumSuscription == "platinum")
            ) {
                Toast.makeText(app, "Change your suscrption to $albumSuscription to listen to this song",
                    Toast.LENGTH_LONG).show()
            } else {
                app.songManager.playSongList(album.song_list)
                for (song in album.song_list)
                    Log.d(TAG, song.song_name)
            }


        }

        val commentsButton = findViewById<Button>(R.id.comments_button)
        commentsButton.setOnClickListener {
            val intent = Intent(this, AlbumMessagesPage::class.java)
            intent.putExtra("albumId", album.album_id)
            intent.putExtra("authorId", album.author_id)
            if (ownAlbum != null)
                intent.putExtra("ownAlbum", true)
            startActivity(intent)
        }

        StarRatingHandler(findViewById(R.id.rBar), findViewById(R.id.averageRating),
                            album.album_id.toInt(), app.getProfileData("id")!!.toInt(), this)
    }

    private fun initRecyclerView(songList: List<Song>) {
        if (songList.isEmpty()) {
            val text: TextView = findViewById(R.id.informationText)
            text.visibility = View.VISIBLE
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SongRecyclerAdapter(songList, R.layout.layout_song_list_album_item, this::onItemClicked, this::onDeleteButtonClicked)
    }

    private fun onDeleteButtonClicked(song: Song, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Do you want to delete this song?")
        alertDialogBuilder.setMessage("This action is irreversible.")
        alertDialogBuilder.setNegativeButton("yes") { _, _ ->
            DeleteSender.deleteSong(this, song.id, position, this::onSongDeletion)
        }
        alertDialogBuilder.setPositiveButton("no", null)
        alertDialogBuilder.show()
    }

    private fun onSongDeletion(position: Int) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        (recyclerView.adapter as SongRecyclerAdapter).deleteItemOfPosition(position)
        itemDeleted = true
    }

    private fun onItemClicked(song: Song) {
        //Do something with the Song
        val app = (this.application as Spotifiubify)
        val userSuscription = app.getProfileData("user_suscription")
        val songSuscription = song.album_suscription
        if ((userSuscription == "free" && (songSuscription == "platinum" || songSuscription == "gold")) ||
            (userSuscription == "gold" && songSuscription == "platinum")
        ) {
            Toast.makeText(app, "Change your suscrption to $songSuscription to listen to this song",
                Toast.LENGTH_LONG).show()
        } else {
            app.songManager.play(song)

            Log.d(TAG, song.song_name +" with id " + song.id.toString() + " made by " + song.artist)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (itemDeleted) {
            startActivity(Intent(this, MainPage::class.java))
            true
        }
        return super.onOptionsItemSelected(item)
    }

}