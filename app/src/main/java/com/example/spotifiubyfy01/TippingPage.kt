package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.spotifiubyfy01.search.Artist
import org.json.JSONObject

class TippingPage : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipping_page)
        val artist = intent.extras?.get("artist") as Artist
        val artistId = artist.id

        val sendTipBtn = findViewById<Button>(R.id.send_tip_btn)
        val amountToTip = findViewById<EditText>(R.id.amount_to_tip)

        sendTipBtn.setOnClickListener {
            sendTip(amountToTip.text.toString(), artistId)
        }
    }

    private fun sendTip(amount: String, artistId: Any?) {

        val url = "https://spotifiubyfy-users.herokuapp.com/users/wallets/sendfounds"

        val requestBody = JSONObject()
        requestBody.put("receiver_id", artistId)
        requestBody.put("foundsToSend", amount)


        val postRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,url,requestBody,
            { response ->
                Toast.makeText(this, "Funds succesfully sent",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfilePage::class.java)
                startActivity(intent)
            },
            { errorResponse ->
                Toast.makeText(this, "Oops, something wrong happened",
                    Toast.LENGTH_SHORT).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), MODE_PRIVATE).getString(getString(R.string.token_key), null)
                return params
            }

        }

        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_playback) {
            startActivity(Intent(this, ReproductionPage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    }
