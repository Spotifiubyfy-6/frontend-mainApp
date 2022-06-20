package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class Wallet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        val walletAddress = findViewById<TextView>(R.id.wallet_address)
        val walletFunds = findViewById<TextView>(R.id.wallet_funds)
        val retireBtn = findViewById<Button>(R.id.retire_button)
        val addressToRetire = findViewById<TextView>(R.id.address_to_retire)
        val amountToRetire = findViewById<TextView>(R.id.amount_to_retire)

        retireBtn.setOnClickListener {
            retireFunds(addressToRetire, amountToRetire, walletFunds)
        }

        fetchWalletAddress(walletAddress)
        fetchWalletFunds(walletFunds)
    }

    private fun retireFunds(
        addressToRetire: TextView,
        amountToRetire: TextView,
        walletFunds: TextView
    ) {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/wallets/retirefounds"

        val requestBody = JSONObject()
        requestBody.put("address", addressToRetire.text.toString())
        requestBody.put("foundsToRetire", amountToRetire.text.toString())


        val postRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,url,requestBody,
            { response ->
                Toast.makeText(this, "Funds succesfully retired",
                Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfilePage::class.java)
                startActivity(intent)
            },
            { errorResponse ->
                val intent = Intent(this, PopUpWindow::class.java).apply {
                    Log.d(ContentValues.TAG, "ERROR: $errorResponse")
                    val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
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

        }

        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
    }

    private fun fetchWalletFunds(walletFunds: TextView) {

        val url = "https://spotifiubyfy-users.herokuapp.com/users/wallets/founds"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                walletFunds.text = "Funds: $response"
            },
            { errorResponse ->
                val intent = Intent(this, PopUpWindow::class.java).apply {
                    Log.d(ContentValues.TAG, "ERROR: $errorResponse")
                    val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
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

        }
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }

    private fun fetchWalletAddress(walletAddress: TextView) {


        val url = "https://spotifiubyfy-users.herokuapp.com/users/wallets/wallet"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                walletAddress.text = "Address: " + response.toString().split('"')[11]
            },
            { errorResponse ->
                val intent = Intent(this, PopUpWindow::class.java).apply {
                    Log.d(ContentValues.TAG, "ERROR: $errorResponse")
                    val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
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
        }
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)

    }
}