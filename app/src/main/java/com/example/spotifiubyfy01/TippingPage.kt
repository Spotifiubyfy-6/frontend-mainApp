package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class TippingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipping_page)

        val artistId = intent.extras?.get("artist_id")

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


        val postRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this, "Funds succesfully Sent",
                    Toast.LENGTH_SHORT).show()
                //fetchWalletFunds(walletFunds)
            },
            { errorResponse ->
                val intent = Intent(this, PopUpWindow::class.java).apply {
                    Log.d(ContentValues.TAG, "ERROR: $errorResponse")
                    val error = errorResponse.networkResponse.data.decodeToString()
                    putExtra("popuptext", error)
                    putExtra("tokenValidation", true)
                }
                startActivity(intent)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), MODE_PRIVATE).getString(getString(R.string.token_key), null)
                return params
            }

            override fun getBody(): ByteArray {
                return requestBody.toString().toByteArray(Charsets.UTF_8)

            }

            override fun getBodyContentType(): String? {
                return "application/json"
            }
        }

        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
    }

    }
