package com.example.spotifiubyfy01

import android.app.Application
import android.content.Context
import android.content.Intent
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.util.*


class Spotifiubify : Application() {
    var profileData: Hashtable<String, String> = Hashtable<String, String>()


    fun setProfile() {
        profileData.clear()
        val url = "https://spotifiubyfy-users.herokuapp.com/users/me"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)
                profileData.put("username", responseJson.getString("username"))
                profileData.put("email", responseJson.getString("email"))
                profileData.put("user_suscription", responseJson.getString("user_suscription"))
                profileData.put("user_type", responseJson.getString("user_type"))
                profileData.put("location", responseJson.getString("location"))
                profileData.put("id", responseJson.getString("id"))
            },
            { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                putExtra("popuptext", error)
                putExtra("tokenValidation", true) }
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
        profileData.put(field, data)
    }

    fun getProfileData(field: String): String? {
        return profileData.get(field)
    }
}