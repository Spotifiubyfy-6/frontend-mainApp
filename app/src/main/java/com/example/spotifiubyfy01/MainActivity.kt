package com.example.spotifiubyfy01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
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
        val email = findViewById<EditText>(R.id.registration_email).text.toString()
        val username = findViewById<EditText>(R.id.registration_username).text.toString()
        val password = findViewById<EditText>(R.id.registration_password).text.toString()

        val requestBody = JSONObject()

        requestBody.put("email", email)
        requestBody.put("username", username)
        requestBody.put("user_type", "listener")
        requestBody.put("password", password)

        val queue = Volley.newRequestQueue(this)
        val url = "https://spotifiubyfy-users.herokuapp.com/users"

        val jsonRequest = JsonObjectRequest(Request.Method.POST, url, requestBody,
            { response -> val intent = Intent(this, DisplayMessageActivity::class.java).apply {
                putExtra("new_password", "Registration successful")
            }
                startActivity(intent)},
            { response -> val intent = Intent(this, PopUpWindow::class.java).apply {
                val error = response.networkResponse.data.decodeToString().split('"')[3]
                putExtra("popuptext", error)
            }
                startActivity(intent)})

        queue.add(jsonRequest)
    }
}