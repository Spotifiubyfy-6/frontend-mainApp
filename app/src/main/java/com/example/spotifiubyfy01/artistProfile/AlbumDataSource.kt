package com.example.spotifiubyfy01.artistProfile

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.spotifiubyfy01.MyRequestQueue
import com.example.spotifiubyfy01.search.Artist
import com.example.spotifiubyfy01.search.VolleyCallBack
import org.json.JSONObject

var image_link = "https://he.cecollaboratory.com/public/layouts/images/group-default-logo.png"

class AlbumDataSource {
    companion object {
        fun createAlbumList(context: Context, artist_id: Int, callBack: VolleyCallBack<Album>) {
            val list = ArrayList<Album>()
            val url = "https://spotifiubyfy-music.herokuapp.com/artists/" + artist_id +
                    "/albums?skip=0&limit=100"
            val getRequest: JsonArrayRequest = object : JsonArrayRequest(
                Method.GET,
                url, null,
                Response.Listener { response ->
                    val size = response.length()
                    for (i in 0 until size) {
                        val jsonArtist = JSONObject(response.get(i).toString())
                        val albumName = jsonArtist.getString("album_name")
                        val id = jsonArtist.getString("id").toInt()
                        list.add(Album(albumName, image_link))
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
                    //    params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), Context.MODE_PRIVATE).getString(getString(R.string.token_key), null)
                    return HashMap()
                }
                }
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }
    }
}
