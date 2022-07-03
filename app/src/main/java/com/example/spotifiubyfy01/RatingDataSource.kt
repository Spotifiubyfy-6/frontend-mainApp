package com.example.spotifiubyfy01

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import kotlin.reflect.KFunction1

class RatingDataSource {
    companion object {
        fun getReviewsAverage(
            context: Context,
            albumId: Int,
            userId: Int,
            callBack: (Int, Int, Int) -> Unit
        ) {
            val url = "http://spotifiubyfy-music.herokuapp.com/reviews?album_id=" +
                    albumId.toString() + "&skip=0&limit=100"
            val getRequest = JsonArrayRequest(
                Request.Method.GET,
                url, null,
                { response ->
                    var reviewSum = 0
                    var userReview = -1
                    val numberOfReviews = response.length()
                    for (i in 0 until numberOfReviews) {
                        val reviewJson = JSONObject(response.get(i).toString())
                        val reviewerId = reviewJson.get("user_id").toString().toInt()
                        val reviewRating = reviewJson.get("quantitative_review").toString().toInt()
                        reviewSum += reviewRating
                        if (reviewerId == userId)
                            userReview = reviewRating
                    }
                    callBack(reviewSum, numberOfReviews, userReview)
                },
                { errorResponse -> val intent = Intent(context, PopUpWindow::class.java).apply {
                    var body = "undefined error"
                    if (errorResponse.networkResponse.data != null) {
                        try {
                            body = String(errorResponse.networkResponse.data, Charsets.UTF_8)
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace()
                        }
                    }
                    putExtra("popuptext", body)
                }
                    startActivity(context, intent, null)
                }
            )
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }

        fun postReview(
            context: Context,
            rating: Float,
            albumId: Int,
            userId: Int,
            changeAverageRating: KFunction1<Float, Unit>
        ) {
            val url = "http://spotifiubyfy-music.herokuapp.com/reviews"
            val jsonRequest: StringRequest = object : StringRequest(
                Method.POST, url, {response ->
                    changeAverageRating(rating)
                },
                { errorResponse ->
                    Log.d("TAG", errorResponse.toString())
                }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    val params2 = HashMap<String, String>()
                    params2["user_id"] = userId.toString()
                    params2["album_id"] = albumId.toString()
                    params2["quantitative_review"] = rating.toInt().toString()
                    return JSONObject(params2 as Map<String, String>).toString().toByteArray()
                }
            }
            MyRequestQueue.getInstance(context).addToRequestQueue(jsonRequest)
        }
    }
}