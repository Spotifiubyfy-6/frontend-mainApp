package com.example.spotifiubyfy01

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class SignInLandingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_landing_page)

        val signInButton = findViewById<Button>(R.id.create_account_button)
        signInButton.disable()

        val email = findViewById<EditText>(R.id.registration_email)
        val username = findViewById<EditText>(R.id.registration_username)
        val password = findViewById<EditText>(R.id.registration_password)

        var validEmail = false
        var validUsername = false
        var validPassword = false

        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!email.text.toString().contains('@')) {
                    email.error = "Please enter an email"
                    validEmail = false
                    signInButton.disable()
                } else {
                    email.error = null
                    validEmail = true
                    if (validEmail and validPassword and validUsername) signInButton.enable()
                }
            }
        })
        username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val regex = """^[A-Za-z][A-Za-z0-9_]{4,29}${'$'}""".toRegex()
                if (!regex.matches(username.text.toString())) {
                    username.error = "Please enter a valid username"
                    validUsername = false
                    signInButton.disable()
                } else {
                    username.error = null
                    validUsername = true
                    if (validEmail and validPassword and validUsername) signInButton.enable()
                }
            }
        })
        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val regex = """^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}${'$'}""".toRegex()
                if (!regex.matches(password.text.toString())) {
                    password.error = "Please use a secure password"
                    validPassword = false
                    signInButton.disable()
                } else {
                    password.error = null
                    validPassword = true
                    if (validEmail and validPassword and validUsername) signInButton.enable()
                }
            }
        })


        signInButton.setOnClickListener {
            val requestBody = JSONObject()

            requestBody.put("email", email.toString())
            requestBody.put("username", username.toString())
            requestBody.put("user_type", "listener")
            requestBody.put("password", password.toString())

            val url = "https://spotifiubyfy-users.herokuapp.com/users"

            val jsonRequest = JsonObjectRequest(Request.Method.POST, url, requestBody,
                { response -> val intent = Intent(this, DisplayMessageActivity::class.java).apply {
                    putExtra("new_password", "Registration successful")
                }
                    startActivity(intent)},
                { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                    val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                    putExtra("popuptext", error)
                }
                    startActivity(intent)})

            MyRequestQueue.getInstance(this).addToRequestQueue(jsonRequest)
        }
    }


    fun View.disable() {
        alpha = .2f
        isClickable = false
    }

    fun View.enable() {
        alpha = 1f
        isClickable = true
    }
}

