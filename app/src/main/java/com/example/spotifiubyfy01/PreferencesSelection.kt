package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray
import org.json.JSONObject


class PreferencesSelection : AppCompatActivity(), AdapterView.OnItemClickListener{

    private var items = arrayOf("")

    private var myInterests = arrayOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_selection)

        fetchInterests()
        //fetchMyInterests()

    }

    public fun fetchInterests() {
        val url = "https://spotifiubyfy-users.herokuapp.com/interests"

        val getRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val list = Array(response.length()) {
                    response.getString(it)
                }
                this.items = list
                val listView : ListView = findViewById(R.id.dynamic_list)
                val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(applicationContext,
                    android.R.layout.simple_list_item_1, items)

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

        val url = "https://spotifiubyfy-users.herokuapp.com/users/interests/$options"
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)
                Toast.makeText(applicationContext, "Added $options",
                Toast.LENGTH_LONG).show()

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
        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)
    }

    private fun fetchMyInterests() {

        val url = "https://spotifiubyfy-users.herokuapp.com/users/interests/my_interests"
        val getRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)

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