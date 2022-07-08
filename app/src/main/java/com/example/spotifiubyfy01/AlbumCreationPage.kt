package com.example.spotifiubyfy01

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.size
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifiubyfy01.search.DataSource
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class AlbumCreationPage : BaseActivity(), AdapterView.OnItemClickListener {
    var albumMediaFile: Uri? = null
    private var albumSuscription: String? = null
    private var albumGenre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_creation_page)
        val app = (this.application as Spotifiubify)

        val albumName = findViewById<EditText>(R.id.albumName)
        val albumDescription = findViewById<EditText>(R.id.albumDescription)

        val findImage = findViewById<Button>(R.id.selectImage)
        findImage.setOnClickListener {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, 1)
        }
        DataSource.getAvailableSuscriptions(this, this::addSuscriptionsToList)
        DataSource.getAvailableIntetests(this, this::addInterestsToList)
        val createAlbumButton = findViewById<Button>(R.id.createAlbumButton)
        createAlbumButton.setOnClickListener {
            val requestBody = JSONObject()

            requestBody.put("album_name", albumName.text.toString())
            requestBody.put("album_description", albumDescription.text.toString())
            requestBody.put("album_genre", albumGenre!!)
            requestBody.put("suscription", albumSuscription!!)
            requestBody.put("artist_id", app.getProfileData("id"))

            val url = "http://spotifiubyfy-music.herokuapp.com/albums"

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response ->
                    val intent = Intent(this, SongCreationPage::class.java).apply {
                    putExtra("album_id", response.getString("id"))
		            putExtra("album_name", albumName.text.toString())
                    if (albumMediaFile != null) {
                        val storageName = "covers/"+response.getString("album_media")
                        val coverRef =  app.getStorageReference().child(storageName)
                        val uploadTask = coverRef.putFile(albumMediaFile!!)
                        uploadTask.addOnFailureListener {
                            Toast.makeText(app, "Cover not uploaded: ERROR", Toast.LENGTH_LONG).show()
                        }.addOnSuccessListener {
                            Toast.makeText(app, "Cover successfully uploaded", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                    startActivity(intent)},
                { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                    var body = "undefined error"
                    if (errorResponse.networkResponse.data != null) {
                        try {
                            body = String(errorResponse.networkResponse.data, Charsets.UTF_8)
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace()
                        }
                    }
                    putExtra("popuptext", body)
                    }
                    startActivity(intent)
                }
            )
            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
        }
    }

    private fun addInterestsToList(interestsList: List<String>) {
        val dropDownMenu = findViewById<AutoCompleteTextView>(R.id.albumGenre)
        albumGenre = interestsList[0]
        dropDownMenu.setText(albumGenre!!)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, interestsList)
        with(dropDownMenu) {
            setAdapter(adapter)
            onItemClickListener = this@AlbumCreationPage
        }
        dropDownMenu.isFocusable = true
    }

    private fun addSuscriptionsToList(suscriptionList: List<String>) {
        val dropDownMenu = findViewById<AutoCompleteTextView>(R.id.albumSuscription)
        albumSuscription = suscriptionList[0]
        dropDownMenu.setText(albumSuscription)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, suscriptionList)
        with(dropDownMenu) {
            setAdapter(adapter)
            onItemClickListener = this@AlbumCreationPage
        }
        dropDownMenu.isFocusable = true
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 1
            && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                this.albumMediaFile = uri
//                val image = findViewById<ImageView>(R.id.album_image)
//                Glide.with(image.context).load(uri).into(image)
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        if (item == null)
            return
        if (suscriptionsMenuWasClicked(parent)) {
            albumSuscription = item
        } else {
            albumGenre = item
        }

    }

    private fun suscriptionsMenuWasClicked(parent: AdapterView<*>?): Boolean {
        if (parent?.size == null)
            return false
        for (i in 0 until parent?.size!!) {
            if (parent?.getItemAtPosition(i).toString().equals(albumSuscription))
                return true
        }
        return false
    }
}
