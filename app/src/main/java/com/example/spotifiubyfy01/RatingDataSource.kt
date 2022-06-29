package com.example.spotifiubyfy01

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONObject

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
                {

                })
            MyRequestQueue.getInstance(context).addToRequestQueue(getRequest)
        }
    }
}