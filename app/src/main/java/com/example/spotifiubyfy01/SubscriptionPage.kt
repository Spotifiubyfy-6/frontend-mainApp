package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.util.HashMap

class SubscriptionPage : NotificationReceiverActivity(), AdapterView.OnItemClickListener {


    private var items = arrayOf("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_page)
        fetchSubscriptionTypes()
        fetchMySubscription()
    }

    private fun fetchMySubscription() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        return true
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


    private fun fetchSubscriptionTypes() {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/user_subscription"

        val getRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->

                val freePrice = response.getString("free")
                val goldPrice = response.getString("gold")
                val platinumPrice = response.getString("platinum")

                this.items = arrayOf("free: $freePrice", "gold: $goldPrice", "platinum: $platinumPrice")
                val listView : ListView = findViewById(R.id.dynamic_list)
                val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(applicationContext,
                    R.layout.list_item_genre, R.id.text_view_for_genre, items)

                listView.adapter = arrayAdapter
                listView.onItemClickListener = this


            },
            { errorResponse ->
                print(errorResponse)
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val options : String = parent?.getItemAtPosition(position) as String
        view?.setBackgroundColor(Color.GREEN);
        val type = options.split(":")[0]
        val url = "https://spotifiubyfy-users.herokuapp.com/users/user_subscription/$type"
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)
                Toast.makeText(applicationContext, "Subscription changed to $type",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfileEditPage::class.java)
                startActivity(intent)


            },
            { errorResponse ->

                Toast.makeText(applicationContext, "Wasnt possible, try again later. $errorResponse",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfileEditPage::class.java)
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
        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
    }

}









