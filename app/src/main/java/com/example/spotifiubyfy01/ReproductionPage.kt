package com.example.spotifiubyfy01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifiubyfy01.artistProfile.Song
import java.util.*


class ReproductionPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reproduction_page)

        val app = (this.application as Spotifiubify)
        var song: Song = app.SongManager.currentSong
        val mediaPlayer = app.SongManager.MediaPlayer
        val currentSongTime = findViewById<TextView>(R.id.position)

        findViewById<TextView>(R.id.title).text = song.song_name
        findViewById<TextView>(R.id.subtitle).text = song.artist
        findViewById<TextView>(R.id.duration).text = convertToMinutesString(mediaPlayer.duration)
        currentSongTime.text = convertToMinutesString(mediaPlayer.currentPosition)


        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (app.SongManager.isPlaying()) {
                        currentSongTime.post { currentSongTime.text = convertToMinutesString(mediaPlayer.currentPosition)
                        }
                    } else {
                        song = app.SongManager.currentSong
                        findViewById<TextView>(R.id.title).text = song.song_name
                        findViewById<TextView>(R.id.subtitle).text = song.artist
                        findViewById<TextView>(R.id.duration).text = convertToMinutesString(mediaPlayer.duration)
                        currentSongTime.text = convertToMinutesString(mediaPlayer.currentPosition)
                        timer.cancel()
                        timer.purge()
                    }
                    if (song != app.SongManager.currentSong) {
                        song = app.SongManager.currentSong
                        findViewById<TextView>(R.id.title).text = song.song_name
                        findViewById<TextView>(R.id.subtitle).text = song.artist
                        findViewById<TextView>(R.id.duration).text = convertToMinutesString(mediaPlayer.duration)
                        currentSongTime.text = convertToMinutesString(mediaPlayer.currentPosition)
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
                app.SongManager.next()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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



