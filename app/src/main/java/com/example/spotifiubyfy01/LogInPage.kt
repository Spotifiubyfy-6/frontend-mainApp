package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import java.io.UnsupportedEncodingException


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
                    startActivity(intent)}) {
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(charset("utf-8"))
                }
            }
            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
        }

    }
}