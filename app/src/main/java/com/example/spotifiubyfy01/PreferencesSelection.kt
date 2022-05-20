package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class PreferencesSelection : AppCompatActivity() {

    var counter = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_selection)

        // Get Intent
        val location = intent.getStringExtra("Location")

        // Set title with location
        val textView = findViewById<TextView>(R.id.textView).apply {
            text = "Welcome from " + location.toString() + "\n"  + text
        }
        findViewById<Button>(R.id.button).apply {
            isEnabled = false
        }
    }

    public fun selectGenre(view : View) {
        // falta chequear que sean distintos botones
        // o que no esten verdes
        counter += 1
        view.setBackgroundColor(Color.GREEN)

        if(counter >= 3) {
            findViewById<Button>(R.id.button).apply {
                isEnabled = true
            }
        }
    }

    public fun confirmGenreSelection(view : View) {

        // Send genres selection to backend
        sendGenreSelection()

        // Go to main page
        val intent = Intent(this, MainPage::class.java).apply {
        }
        startActivity(intent)
    }

    private fun sendGenreSelection() {
        /**
        val url = "https://spotifiubyfy-users.herokuapp.com/users/me"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)
                findViewById<TextView>(R.id.username).text = responseJson.getString("username")
                findViewById<TextView>(R.id.email).text = responseJson.getString("email")
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
        */
    }

}