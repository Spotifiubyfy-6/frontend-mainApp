package com.example.spotifiubyfy01

import android.os.Bundle
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
        var song: Song? = app.SongManager.getCurrent()
        if (song == null) {
            song = Song("songName", "artist_name", 1, 1, "storageName")
        }

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
                    if (mediaPlayer.isPlaying) {
                        currentSongTime.post { currentSongTime.text = convertToMinutesString(mediaPlayer.currentPosition)
                        }
                    } else {
                        timer.cancel()
                        timer.purge()
                    }
                }
            }
        }, 0, 1000)

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



