package com.example.spotifiubyfy01

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.search.Artist
import org.json.JSONObject

class ProfileEditPage : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit_page)
        val app = (this.application as Spotifiubify)

        findViewById<TextView>(R.id.artistName).text = app.getProfileData("name")
        findViewById<TextView>(R.id.usernameView).text = app.getProfileData("username")
        findViewById<TextView>(R.id.email).text = app.getProfileData("email")
        findViewById<TextView>(R.id.subscription).text = app.getProfileData("user_suscription")

        val logOutClick = findViewById<Button>(R.id.logOutButton)
        logOutClick.setOnClickListener {
            val sharedPref = getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(getString(R.string.token_key), "")
                apply()
            }
            val intent = Intent(this, MainLandingPage::class.java)
            startActivity(intent)
        }

        val changeSubscriptionBtn = findViewById<Button>(R.id.subscriptionBtn)
        changeSubscriptionBtn.setOnClickListener {
            val intent = Intent(this, SubscriptionPage::class.java)
            startActivity(intent)
        }

        val changeProfileImage = findViewById<Button>(R.id.changePhotoButton)
        changeProfileImage.setOnClickListener {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, 1)
        }

        val email = findViewById<EditText>(R.id.email)
        val artistName = findViewById<EditText>(R.id.artistName)
        val editEmailClick = findViewById<Button>(R.id.editEmailButton)
        editEmailClick.setOnClickListener {

            var url = "https://spotifiubyfy-users.herokuapp.com/users/email/"+email.text.toString()

            val postRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                { response -> val intent = Intent(this, ProfilePage::class.java).apply {
                        val responseJson = JSONObject(response)
                        val email = responseJson.getString("email")
                        app.setProfileData("email", email)
                        val artist = Artist(app.getProfileData("id").toString().toInt(),
                            app.getProfileData("username").toString(),
                            "profilePictures/"+app.getProfileData("username").toString())
                        intent.putExtra("Artist", artist)
                    }
                    startActivity(intent)
                },
                { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                    val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                    putExtra("popuptext", error)
                }
                    startActivity(intent)}) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                    return params
                }
            }

            MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
        }

        val editArtistNameClick = findViewById<Button>(R.id.editArtistNameButton)
        editArtistNameClick.setOnClickListener {
            val url = "https://spotifiubyfy-users.herokuapp.com/users/name/"+artistName.text.toString()

            val namePostRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                { response -> val intent = Intent(this, ProfilePage::class.java).apply {
                    val responseJson = JSONObject(response)
                    val newName = responseJson.getString("name")
                    app.setProfileData("name", newName)
                }
                    startActivity(intent)
                },
                { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                    val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                    putExtra("popuptext", error)
                }
                    startActivity(intent)}) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                    return params
                }
            }
            MyRequestQueue.getInstance(this).addToRequestQueue(namePostRequest)
        }

        val oldPassword = findViewById<EditText>(R.id.oldPassword)
        val newPassword = findViewById<EditText>(R.id.newPassword)
        val changePasswordClick = findViewById<Button>(R.id.changePasswordButton)
        changePasswordClick.isEnabled = false

        newPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val regex = """^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}${'$'}""".toRegex()
                if (!regex.matches(newPassword.text.toString())) {
                    newPassword.error = "Please use a secure password"
                } else {
                    newPassword.error = null
                    changePasswordClick.isEnabled = true
                }
            }
        })

        changePasswordClick.setOnClickListener {
            val requestBody = JSONObject()

            requestBody.put("old_password", oldPassword.text.toString())
            requestBody.put("new_password", newPassword.text.toString())

            val url = "https://spotifiubyfy-users.herokuapp.com/users/change_password"

            val jsonRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, requestBody,
                {
                    val intent = Intent(this, ProfilePage::class.java).apply {
                    putExtra("passwordChangeSuccess", "Your password was successfully changed")
                }
                    startActivity(intent)},
                { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                    val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                    putExtra("popuptext", error)
                }
                    startActivity(intent)}) {
                @Throws(com.android.volley.AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), MODE_PRIVATE).getString(getString(
                        R.string.token_key), null)
                    return params
                    }
                }

            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
        }
    }
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 1
            && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { uri ->
                val app = (this.application as Spotifiubify)
                val storageName = "profilePictures/"+app.getProfileData("username").toString()
                val coverRef =  app.getStorageReference().child(storageName)
                val uploadTask = coverRef.putFile(uri)
                uploadTask.addOnFailureListener {
                    Toast.makeText(app, "Profile picture not uploaded, try again later", Toast.LENGTH_LONG).show()
                }.addOnSuccessListener {
                    Toast.makeText(app, "Profile picture successfully uploaded", Toast.LENGTH_SHORT).show()
                    val url = "https://spotifiubyfy-users.herokuapp.com/users/photo/"+app.getProfileData("username").toString()
                    val jsonRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, null,
                        {
                            val intent = Intent(this, ProfilePage::class.java)
                            startActivity(intent)},
                        {}) {
                        @Throws(com.android.volley.AuthFailureError::class)
                        override fun getHeaders(): Map<String, String> {
                            val params: MutableMap<String, String> = HashMap()
                            params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), MODE_PRIVATE).getString(getString(
                                R.string.token_key), null)
                            return params
                        }
                    }
                    MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
                }
            }
        }
    }
    fun editPreferences(view : View) {
        val intent = Intent(this, PreferencesSelection::class.java).apply {
            val location = "a place in the World"
            putExtra("Location", location)
        }
        startActivity(intent)
    }

    fun editLocation(view : View) {
        val intent = Intent(this, LocationSelection::class.java)
        startActivity(intent)
    }
}