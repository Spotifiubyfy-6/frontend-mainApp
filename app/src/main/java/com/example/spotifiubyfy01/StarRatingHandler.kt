package com.example.spotifiubyfy01

import android.util.Log
import android.widget.RatingBar
import android.widget.TextView

class StarRatingHandler(val starBar: RatingBar, val findViewById: TextView, val userId: Int) {

    fun watchNUpdateRating() {
        starBar.rating = 4F
        starBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            Log.d("TAG", rating.toString())
        }
    }
}