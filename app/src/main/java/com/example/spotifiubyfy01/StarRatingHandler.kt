package com.example.spotifiubyfy01

import android.content.Context
import android.widget.RatingBar
import android.widget.TextView

class StarRatingHandler(private val starBar: RatingBar, private val averageRating: TextView,
                        val albumId: Int, private val userId: Int, val context: Context) {
    private var userCurrentReview = -1
    private var reviewSum = 0
    private var numberOfReviews = 0
    init {
        RatingDataSource.getReviewsAverage(context, albumId, userId, this::setAlbumAverage)
    }

    private fun watchNUpdateRating() {
        starBar.setOnRatingBarChangeListener { _, rating, _ ->
            RatingDataSource.postReview(context, rating, albumId, userId,
                this::changeAverageRating)
        }
    }

    private fun changeAverageRating(rating: Float) {
        if (averageRating.text.contains("NR")) { //first review
            val averageString = "%.1f".format(rating) + "/5"
            averageRating.text = averageString
            return
        }

        val ratingInt = rating.toInt()
        if (userCurrentReview != -1) {  //user has rated
            reviewSum = reviewSum - userCurrentReview + ratingInt
        } else {//user has not rated
            reviewSum += ratingInt
            numberOfReviews++
        }
        userCurrentReview = ratingInt
        val averageString = "%.1f".format((reviewSum.toFloat() / numberOfReviews.toFloat())) + "/5"
        averageRating.text = averageString
    }

    private fun setAlbumAverage(reviewSum: Int, numberOfReviews: Int, userReview: Int) {
        this.numberOfReviews = numberOfReviews
        this.userCurrentReview = userReview
        this.reviewSum = reviewSum

        if (userCurrentReview >= 0)
            starBar.rating = userReview.toFloat()
        else
            starBar.rating = 0F

        val averageString = StringBuffer()
        if (numberOfReviews == 0)
            averageString.append("NR")
        else {
            val average = "%.1f".format((reviewSum.toFloat() / numberOfReviews.toFloat()))
            averageString.append(average)
        }
        averageString.append("/5")
        averageRating.text = averageString
        this.watchNUpdateRating()
    }
}