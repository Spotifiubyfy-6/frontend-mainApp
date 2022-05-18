package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class ProfileEditPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit_page)
        getProfileData()

        val email = findViewById<EditText>(R.id.email)

        val editClick = findViewById<Button>(R.id.editButton)
        editClick.setOnClickListener {

            val url = "https://spotifiubyfy-users.herokuapp.com/users/email/"+email.text.toString()

            val postRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                { val intent = Intent(this, ProfilePage::class.java).apply {
                }
                    startActivity(intent)},
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

    private fun getProfileData() {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/me"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val responseJson = JSONObject(response)
                findViewById<EditText>(R.id.username).setText(responseJson.getString("username"))
                findViewById<EditText>(R.id.email).setText(responseJson.getString("email"))
                findViewById<TextView>(R.id.subscription).text = responseJson.getString("user_suscription")

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
}