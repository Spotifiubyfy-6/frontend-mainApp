package com.example.spotifiubyfy01.search

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.MyRequestQueue
import org.json.JSONObject

class DataSource{
    companion object {
        fun updateDataSet(context: Context, slice: String, callBack: VolleyCallBack) {
            val list = ArrayList<Artist>()
            if (slice.isEmpty()) {
                callBack.updateDataInRecyclerView(list)
                return
            }
            val url = "https://spotifiubyfy-users.herokuapp.com/users/information/" + slice +
                    "?skip=0&limit=10"
            val getRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                url, null,
                Response.Listener { response ->
                    val size = response.length()
                    for (i in 0 until size) {
                        val jsonArtist = JSONObject(response.get(i).toString())
                        val username = jsonArtist.getString("username")
                        list.add(Artist(username, image_link))
                    }
                    callBack.updateDataInRecyclerView(list)
                },
                { errorResponse ->
                 /*   val intent = Intent(context, PopUpWindow::class.java).apply {
                        val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                        putExtra("popuptext", error)
                        putExtra("tokenValidation", true)
                    }
                    startActivity(intent)
             */ }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                //    params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                    return params
                }
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }
    }
}