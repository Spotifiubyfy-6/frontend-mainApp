package com.example.spotifiubyfy01
import android.content.Context.WIFI_SERVICE
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.WifiLock
import android.os.PowerManager
import android.widget.Toast
import com.example.spotifiubyfy01.artistProfile.Song
import java.util.*


class SongManager(app: Spotifiubify) {
    val MediaPlayer = MediaPlayer()
    val playlist = LinkedList<Song>()
    private var wifiLock:  WifiLock


    init {
        MediaPlayer.setWakeMode(app, PowerManager.PARTIAL_WAKE_LOCK)
        val wifiManager = app.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "wifi_lock")
    }

    fun play(song: Song, app: Spotifiubify) {
        playlist.push(song)
        wifiLock.acquire()
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
                isLooping = false
                setOnCompletionListener {
                    playlist.remove()
                    if (playlist.isEmpty()) {
                        wifiLock.release()
                    } else {
                        //play next song on que
                    }
                }
                setDataSource(url.toString())
                prepare()
                start()
            }
        }.addOnFailureListener {
            Toast.makeText(app, "couldn't play song", Toast.LENGTH_LONG).show()
        }
    }

//    fun pause() {
//        MediaPlayer.pause()
//    }

//    fun start

    fun getCurrent(): Song? {
        return playlist.peekFirst()
    }

}