package com.example.spotifiubyfy01

import android.content.Context
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView

class StarRatingHandler(private val starBar: RatingBar, val averageRating: TextView,
                        val albumId: Int, val userId: Int, val context: Context) {
    private var userHadRated = false

    init {
        RatingDataSource.getReviewsAverage(context, albumId, userId, this::setAlbumAverage)
    }

    private fun watchNUpdateRating() {
        starBar.setOnRatingBarChangeListener { _, rating, _ ->
            RatingDataSource.postReview(context, rating.toInt(), albumId, userId)
            changeAverageRating(rating)
        }
    }

    private fun changeAverageRating(rating: Float) {
        if (!userHadRated) {
            if (averageRating.text.contains("NR")) { //first review
                val averageString = "%.1f".format(rating) + "/5"
                averageRating.text = averageString
                return
            }
        }

    }

    private fun setAlbumAverage(reviewSum: Int, numberOfReviews: Int, userReview: Int) {
        if (userReview >= 0) {
            starBar.rating = userReview.toFloat()
            userHadRated = true
        } else {
            starBar.rating = 0F
        }
        val averageString = StringBuffer()
        if (numberOfReviews == 0)
            averageString.append("NR")
        else {
            Log.d("TAG", reviewSum.toString())
            Log.d("TAG", numberOfReviews.toString())
            val average = "%.1f".format((reviewSum / numberOfReviews).toFloat())
            averageString.append(average)
        }
        averageString.append("/5")
        averageRating.text = averageString
        this.watchNUpdateRating()
    }
}