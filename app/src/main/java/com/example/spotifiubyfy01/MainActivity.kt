package com.example.spotifiubyfy01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val username = findViewById<EditText>(R.id.registration_username).text.toString()
        val password = findViewById<EditText>(R.id.registration_password).text.toString()

        val requestBody = JSONObject()

        requestBody.put("email", "unEjemploHardcodeado@fi.uba.ar")
        requestBody.put("username", username)
        requestBody.put("user_type", "listener")
        requestBody.put("password", password)

        val queue = Volley.newRequestQueue(this)
        val url = "https://spotifiubyfy-users.herokuapp.com/docs#/default/register_new_user_users_post"

        val jsonRequest = JsonObjectRequest(Request.Method.POST, url, requestBody,
            { response -> val intent = Intent(this, DisplayMessageActivity::class.java).apply {
                putExtra("new_username", response.toString())
            }
                startActivity(intent)},
            { response -> val intent = Intent(this, DisplayMessageActivity::class.java).apply {
                putExtra("new_username", response.toString())
            }
                startActivity(intent)})

        queue.add(jsonRequest)
    }
}