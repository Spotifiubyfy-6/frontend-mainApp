package com.example.spotifiubyfy01

import android.content.Context
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView

class StarRatingHandler(private val starBar: RatingBar, val averageRating: TextView,
                        val albumId: Int, userId: Int, val context: Context) {
    private var userHasRated = false
    init {
        RatingDataSource.getReviewsAverage(context, albumId, userId, this::setAlbumAverage)
    }
    fun watchNUpdateRating() {

        starBar.setOnRatingBarChangeListener { _, rating, _ ->
            Log.d("TAG", rating.toString())
        }
    }

    private fun setAlbumAverage(reviewSum: Int, numberOfReviews: Int, userReview: Int) {
        if (userReview >= 0) {
            starBar.rating = userReview.toFloat()
            userHasRated = true
        } else {
            starBar.rating = 0F
        }
        val averageString = StringBuffer()
        if (numberOfReviews == 0)
            averageString.append("NF")
        else {
            val average = reviewSum / numberOfReviews
            averageString.append(average.toString())
        }
        averageString.append("/5")
        averageRating.text = averageString
    }
}