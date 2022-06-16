package com.example.spotifiubyfy01

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifiubyfy01.artistProfile.Album
import org.json.JSONObject
import java.io.File

class SongCreationPage : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var albumId: Int? = null
    private var albumList: ArrayList<Album>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_creation_page)
        val dropDownMenu = findViewById<AutoCompleteTextView>(R.id.albums_names)
        albumList = intent.extras?.get("albums") as ArrayList<Album>?
        val albumNames = ArrayList<String>()
        var albumName = ""
        if (albumList == null) {
            albumName = intent.extras?.get("album_name") as String
            albumNames.add(albumName)
            albumId = (intent.getStringExtra("album_id") as String).toInt()
        } else {
            obtainAlbumNamesAndAddToList(albumList!!, albumNames)
            albumId = obtainAlbumIdOfPosition(albumList!!, 0)
        }
        dropDownMenu.setText(albumNames[0])
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, albumNames)
        with(dropDownMenu) {
            setAdapter(adapter)
            onItemClickListener = this@SongCreationPage
        }
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
//                Toast.makeText(this, "Permissions granted",Toast.LENGTH_SHORT).show()
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
            requestBody.put("album_id", albumId.toString())

            val url = "http://spotifiubyfy-music.herokuapp.com/music"

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response -> val intent = Intent(this, SongCreationPage::class.java).apply {
                    putExtra("songCreated", "Song successfully created")
                    putExtra("album_id", intent.getStringExtra("album_id"))
                    putExtra("albums", albumList)
                    putExtra("album_name", albumName)

                    val storageName = "songs/"+response.getString("storage_name")
                    val songRef =  app.getStorageReference().child(storageName)
                    val file = Uri.fromFile(File(songPath.text.toString()))
                    val uploadTask = songRef.putFile(file)
                    uploadTask.addOnFailureListener {
                        Toast.makeText(app, "Song not uploaded: ERROR",Toast.LENGTH_LONG).show()
                    }.addOnSuccessListener {
                        Toast.makeText(app, "Song successfully uploaded",Toast.LENGTH_SHORT).show()
                    }
                }
                    startActivity(intent)},
                { val intent = Intent(this, PopUpWindow::class.java).apply {
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

    private fun obtainAlbumIdOfPosition(albumList: ArrayList<Album>, position: Int): Int {
        return albumList[position].album_id.toInt()
    }

    private fun obtainAlbumNamesAndAddToList(albumList: ArrayList<Album>,
                                             albumNames: ArrayList<String>) {
        for (album in albumList)
            albumNames.add(album.album_name)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val albumName = parent?.getItemAtPosition(position).toString()
        if (albumList == null)
            return
        albumId = obtainAlbumIdOfAlbumWithName(albumList!!, albumName)
    }

    private fun obtainAlbumIdOfAlbumWithName(albumList: ArrayList<Album>, albumName: String): Int {
        for (album in albumList) {
            if (album.album_name.equals(albumName))
                return album.album_id.toInt()
        }
        return 0 //should not happen
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_playback) {
            startActivity(Intent(this, ReproductionPage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}

//  /sdcard/Download/shrek.mp3  para la demo
