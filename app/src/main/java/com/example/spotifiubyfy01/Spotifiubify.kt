package com.example.spotifiubyfy01

import android.app.Application
import android.content.Context
import android.content.Intent
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.json.JSONObject
import java.util.*
import com.example.spotifiubyfy01.SongManager
import com.example.spotifiubyfy01.artistProfile.Song


class Spotifiubify : Application() {
    var profileData: Hashtable<String, String> = Hashtable<String, String>()
    private lateinit var storage: FirebaseStorage
    lateinit var SongManager: SongManager

    override fun onCreate() {
        super.onCreate()
        storage = Firebase.storage
        SongManager = SongManager(this)
    }

    fun setProfile() {
        profileData.clear()
        val url = "https://spotifiubyfy-users.herokuapp.com/users/me"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)
                profileData["username"] = responseJson.getString("username")
                profileData["email"] = responseJson.getString("email")
                profileData["user_suscription"] = responseJson.getString("user_suscription")
                profileData["user_type"] = responseJson.getString("user_type")
                profileData["location"] = responseJson.getString("location")
                profileData["id"] = responseJson.getString("id")
                profileData["name"] = responseJson.getString("name")

            },
            { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                putExtra("popuptext", error)
                putExtra("tokenValidation", true) }
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                return params
            }
        }
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    fun setProfileData(field: String, data: String) {
        profileData[field] = data
    }

    fun getProfileData(field: String): String? {
        return profileData[field]
    }

    fun getStorageReference(): StorageReference {
        return storage.reference
    }
}