package com.example.spotifiubyfy01

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifiubyfy01.artistProfile.Album
import org.json.JSONObject
import java.io.File

class SongCreationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_creation_page)
        val dropDownMenu = findViewById<AutoCompleteTextView>(R.id.albums_names)
        val albumList = intent.extras?.get("albums") as ArrayList<Album>?
        val albumNames = ArrayList<String>()
        if (albumList == null) {
            val albumName = intent.extras?.get("album_name") as String
            albumNames.add(albumName)
        } else {
            obtainAlbumNamesAndAddToList(albumList, albumNames)
        }
        dropDownMenu.setText(albumNames[0])
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, albumNames)
        dropDownMenu.setAdapter(adapter)
//      falta hacer esto mas explicativo, que se entienda mejor cuando se dan los permisos y eso
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(this, "Permissions granted",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permissions are necessary to upload songs",Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                Toast.makeText(this, "Permissions granted",Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }

        if ( !intent.getStringExtra("songCreated").isNullOrEmpty()) {
            Toast.makeText(this, intent.getStringExtra("songCreated"),Toast.LENGTH_SHORT).show()
        }

        val app = (this.application as Spotifiubify)

        val songName = findViewById<EditText>(R.id.songName)
        val songDescription = findViewById<EditText>(R.id.songDescription)
        val songPath = findViewById<EditText>(R.id.songPath)

        val uploadSongButton = findViewById<Button>(R.id.upload_song_button)
        uploadSongButton.setOnClickListener {
            val requestBody = JSONObject()

            requestBody.put("song_name", songName.text.toString())
            requestBody.put("song_description", songDescription.text.toString())
            requestBody.put("album_id", intent.getStringExtra("album_id"))

            val url = "http://spotifiubyfy-music.herokuapp.com/music"

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response -> val intent = Intent(this, SongCreationPage::class.java).apply {
                    putExtra("songCreated", "Song successfully created")
                    // puede ser que haya que agregar al bundle el album_id
                    val storageName = "songs/"+response.getString("storage_name")
                    val songRef =  app.getStorageReference().child(storageName)
                    val file = Uri.fromFile(File(songPath.text.toString()))
                    val uploadTask = songRef.putFile(file)
                    putExtra("album_id", intent.getStringExtra("album_id"))
                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener {
                        Toast.makeText(app, "Song not uploaded: ERROR",Toast.LENGTH_LONG).show()
                    }.addOnSuccessListener { taskSnapshot ->
                        Toast.makeText(app, "Song successfully uploaded",Toast.LENGTH_SHORT).show()
                    }
                }
                    startActivity(intent)},
                { val intent = Intent(this, PopUpWindow::class.java).apply {
//                    val error = errorResponse//.networkResponse.data.decodeToString() //.split('"')[3]
                    putExtra("popuptext", "cant create album right now")
                }
                    startActivity(intent)})

            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
        }
        val finishAlbumCreationButton = findViewById<Button>(R.id.finishCreation)
        finishAlbumCreationButton.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }
    }

    private fun obtainAlbumNamesAndAddToList(albumList: ArrayList<Album>,
                                             albumNames: ArrayList<String>) {
        for (album in albumList)
            albumNames.add(album.album_name)
    }
}

//  /sdcard/Download/shrek.mp3  para la demo
