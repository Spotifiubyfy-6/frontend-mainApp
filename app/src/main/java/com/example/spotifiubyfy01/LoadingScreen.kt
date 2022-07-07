package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class LoadingScreen : NotificationReceiverActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)
        validateToken()

    }

    private fun validateToken() {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/me"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener {
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            },
            { val intent = Intent(this, MainLandingPage::class.java)
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
}