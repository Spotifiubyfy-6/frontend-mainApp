package com.example.spotifiubyfy01

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject


class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        get_profile_data()

    }

    fun get_profile_data() {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/me"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)
                findViewById<TextView>(R.id.username).text = responseJson.getString("username")
            },
            Response.ErrorListener { error -> // TODO Auto-generated method stub
                findViewById<TextView>(R.id.username).text =  error.networkResponse.data.decodeToString().split('"')[3] //todo mandar un popup diciendo que caduco el token y que debe logearse de vuelta, volver a la pantalla de inicio
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
}