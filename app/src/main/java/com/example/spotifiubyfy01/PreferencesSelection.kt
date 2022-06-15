package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class PreferencesSelection : AppCompatActivity(), AdapterView.OnItemClickListener{

    private var items = arrayOf("")

    private var myInterests = mutableListOf<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_selection)

        fetchInterests()



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
                    R.layout.list_item_genre, R.id.text_view_for_genre, items)

                listView.adapter = arrayAdapter
                listView.onItemClickListener = this


                fetchMyInterests()
            },
            { errorResponse ->
                print(errorResponse)
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)
    }



    fun deleteInterest(view: View?, options: String) {
        view?.setBackgroundColor(Color.GREEN);
        val url = "https://spotifiubyfy-users.herokuapp.com/interest/$options"
        val postRequest: StringRequest = object : StringRequest(
            Method.DELETE, url,
            Response.Listener { response -> // response

                view?.setBackgroundColor(Color.BLACK);
                Toast.makeText(applicationContext, "Genre $options removed from interests",
                    Toast.LENGTH_SHORT).show()

            },
            { errorResponse ->
                Toast.makeText(applicationContext, "Couldnt remove $options from interests",
                    Toast.LENGTH_SHORT).show()

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

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val options : String = parent?.getItemAtPosition(position) as String
        view?.setBackgroundColor(Color.GREEN);
        val url = "https://spotifiubyfy-users.herokuapp.com/users/interests/$options"
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response -> // response
                val responseJson = JSONObject(response)
                Toast.makeText(applicationContext, "Added $options",
                Toast.LENGTH_SHORT).show()

            },
            { errorResponse ->
                deleteInterest(view, options)


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
                val responseJson = JSONArray(response)
                (0 until responseJson.length()).forEach {
                    val selection = responseJson.getJSONObject(it)
                    val genre = selection.getString("interest")
                    myInterests.add(genre)
                    Log.d(ContentValues.TAG, "INTEREST ADDED: $genre")

                }
                val lstView : ListView = findViewById<ListView>(R.id.dynamic_list)
                val childCount : Int = lstView.childCount - 1
                Log.d(ContentValues.TAG, "LISTAN DE CHILD: $childCount")

                for(i in 0..childCount) {
                    Log.d(ContentValues.TAG, "RELAAA: " + lstView.getChildAt(i))
                    val relativeLayout : RelativeLayout = lstView.getChildAt(i) as RelativeLayout
                    val textView : TextView = relativeLayout.getChildAt(0) as TextView
                    Log.d(ContentValues.TAG, "EL TEXTO ES : " + textView.text.toString())
                    if (this.myInterests.contains(textView.text.toString().lowercase(Locale.ROOT))) {
                        relativeLayout.setBackgroundColor(Color.GREEN)
                    }
                }


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