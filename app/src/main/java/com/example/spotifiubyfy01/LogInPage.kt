package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import kotlin.reflect.KFunction1


class LogInPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_page)

        val logInClick = findViewById<Button>(R.id.log_in_button)
        logInClick.setOnClickListener {
            val username = findViewById<EditText>(R.id.logIn_username).text.toString()
            val password = findViewById<EditText>(R.id.logIn_password).text.toString()

            val requestBody =
                "grant_type=&username=$username&password=$password&scope=&client_id=&client_secret="

            val url = "https://spotifiubyfy-users.herokuapp.com/token"

            val jsonRequest: StringRequest = object : StringRequest(
                Method.POST, url, { response ->
                    val intent = Intent(this, MainPage::class.java).apply {
                    val sharedPref = getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putString(getString(R.string.token_key), response.split('"')[3])
                        apply()
                    }

                }
                    Log.d("TAG", "logging in")
                    getId(this::setNotification)
                    startActivity(intent)},
                { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
//                    val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                    putExtra("popuptext", "error failed log in")
                }
                    startActivity(intent)}) {
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(charset("utf-8"))
                }
            }
            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
        }
    }

    private fun getId(setNotification: KFunction1<String, Unit>) {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/me"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)
                setNotification(responseJson.getString("id").toString())
            },
            { errorResponse ->
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = java.util.HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                return params
            }
        }
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun setNotification(id: String) {
        Log.d("TAG", "User with id: " + id)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            val token = it.result
            val url = "https://spotifiubyfy-messages.herokuapp.com/messages/set_notification_token"
            val jsonRequest: StringRequest = object : StringRequest(
                Method.POST, url, { response ->
                    Log.d("TAG", "Token set")
                                  },
                { errorResponse ->
                    Log.d("TAG", errorResponse.toString())
                }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    val params2 = HashMap<String, String>()
                    params2["user_id"] = id
                    params2["token"] = token
                    return JSONObject(params2 as Map<String, String>).toString().toByteArray()
                }
            }
            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
            Log.d("TAG", token)
        }
    }

}