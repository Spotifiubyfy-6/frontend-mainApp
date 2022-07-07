package com.example.spotifiubyfy01

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spotifiubyfy01.search.Artist
import org.json.JSONObject

class TippingPage : NotificationReceiverActivity() {

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
            Method.POST,url,requestBody,
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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.home -> {
            startActivity(Intent(this, MainPage::class.java))
            true
        }
        R.id.action_playback -> {
            startActivity(Intent(this, ReproductionPage::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    }
