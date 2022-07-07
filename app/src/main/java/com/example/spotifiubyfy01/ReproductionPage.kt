package com.example.spotifiubyfy01

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.spotifiubyfy01.artistProfile.Song
import com.example.spotifiubyfy01.artistProfile.adapter.default_album_image
import java.util.*


class ReproductionPage : NotificationReceiverActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reproduction_page)

        val app = (this.application as Spotifiubify)
        var song: Song = app.songManager.currentSong
        val mediaPlayer = app.songManager.mediaPlayer
        val albumImage = findViewById<ImageView>(R.id.albumArt)
        val currentSongTime = findViewById<TextView>(R.id.position)

        changeView(song, mediaPlayer)

        val coverRef = app.getStorageReference().child(song.album_cover)
        coverRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(albumImage.context).load(url).into(albumImage)
        }.addOnFailureListener {
            Glide.with(albumImage.context).load(default_album_image).into(albumImage)
        }
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (app.songManager.isPlaying()) {
                        currentSongTime.post { currentSongTime.text = convertToMinutesString(mediaPlayer.currentPosition)
                        }
                    } else {
                        song = app.songManager.currentSong
                        changeView(song, mediaPlayer)
                        timer.cancel()
                        timer.purge()
                    }
                    if (song != app.songManager.currentSong) {
                        song = app.songManager.currentSong
                        changeView(song, mediaPlayer)
                    }
                }
            }
        }, 0, 500)

        val pauseButton = findViewById<ImageButton>(R.id.media_button)
        if (mediaPlayer.isPlaying) {
            pauseButton.setImageResource(R.drawable.ic_pause_black_24dp)
        }

        pauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            } else {
                try {
                    mediaPlayer.start()
                } catch(e: IllegalStateException) {
                    Toast.makeText(this, "No song on que", Toast.LENGTH_SHORT).show()
                }
                pauseButton.setImageResource(R.drawable.ic_pause_black_24dp)
            }
        }

        val nextButton = findViewById<ImageButton>(R.id.nextButton)

        nextButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                if (!app.songManager.next()) {
                    pauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                    changeView(song, mediaPlayer)
                    Glide.with(albumImage.context).load(default_album_image).into(albumImage)
                }
            }
        }

    }

    private fun changeView(song: Song, mediaPlayer: MediaPlayer) {
        findViewById<TextView>(R.id.title).text = song.song_name
        findViewById<TextView>(R.id.subtitle).text = song.artist
        findViewById<TextView>(R.id.duration).text = convertToMinutesString(mediaPlayer.duration)
        findViewById<TextView>(R.id.position).text = convertToMinutesString(mediaPlayer.currentPosition)
    }

    private fun convertToMinutesString(time: Int): String {
        if (time == 5832704) return "-"  //underflow hardcodeado xd
        val minutes= time / 1000 / 60
        val seconds = time / 1000 % 60
        if (seconds < 10) {
            return "$minutes:0$seconds"
        }
        return "$minutes:$seconds"
    }
}



