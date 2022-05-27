package com.example.spotifiubyfy01
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import java.util.*
import com.example.spotifiubyfy01.artistProfile.Song


class SongManager {
    val MediaPlayer = MediaPlayer()
    val playlist = LinkedList<Song>()



    fun play(song: Song, app: Spotifiubify) {
        val storageName = "songs/"+song.storage_name
        val songRef = app.getStorageReference().child(storageName)
        songRef.downloadUrl.addOnSuccessListener { url ->
            MediaPlayer.apply {
                reset()
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url.toString())
                prepare() // might take long! (for buffering, etc)
                start()
            }
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(app, "couldnt play song", Toast.LENGTH_LONG).show()
        }
    }
}