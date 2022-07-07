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
import kotlin.NoSuchElementException


class SongManager(app: Spotifiubify) {
    val mediaPlayer = MediaPlayer()
    val playlist = LinkedList<Song>()
    var currentSong: Song
    private var wifiLock:  WifiLock
    private var app: Spotifiubify
    private val nullSong =  Song("songName", "artist_name", 1,1, "storageName", "https://i.pinimg.com/originals/33/58/0c/33580cd023504630a4ea63fe0a1650f6.jpg")

    init {
        mediaPlayer.setWakeMode(app, PowerManager.PARTIAL_WAKE_LOCK)
        val wifiManager = app.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "wifi_lock")
        this.app = app
        currentSong = nullSong
    }

    fun play(song: Song) {
        currentSong = song
        wifiLock.acquire()
        val storageName = "songs/"+currentSong.storage_name
        val songRef = app.getStorageReference().child(storageName)
        songRef.downloadUrl.addOnSuccessListener { url ->
            mediaPlayer.apply {
                reset()
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                isLooping = false
                setOnCompletionListener {
                    try {
                        play(playlist.pop())
                    } catch(e: NoSuchElementException) {
                        currentSong = nullSong
                        if (wifiLock.isHeld) {
                            wifiLock.release()
                        }
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

    fun next(): Boolean{
        return try {
            play(playlist.pop())
            true
        } catch(e: NoSuchElementException) {
            mediaPlayer.stop()
            currentSong = nullSong
            false
        }
    }

    fun isPlaying(): Boolean {
        return currentSong != nullSong
    }

    fun playSongList(songs: List<Song>) {
        playlist.clear()
        playlist.addAll(songs)
        play(playlist.pop())
    }
}