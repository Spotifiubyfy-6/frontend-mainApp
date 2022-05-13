package com.example.spotifiubyfy01

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils

class PopUpWindow : AppCompatActivity() {
    private var popupTitle = ""
    private var popupText = ""
    private var popupButton = ""
    private var darkStatusBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.popup_window)

        // Get the data
        val bundle = intent.extras
        popupTitle = bundle?.getString("popuptitle", "Error") ?: ""
        popupText = bundle?.getString("popuptext", "Retry") ?: ""
        popupButton = bundle?.getString("popupbtn", "OK") ?: ""
        darkStatusBar = bundle?.getBoolean("darkstatusbar", false) ?: false

        // Set the data
        findViewById<AppCompatTextView>(R.id.popup_window_title).text = popupTitle
        findViewById<AppCompatTextView>(R.id.popup_window_text).text = popupText
        findViewById<Button>(R.id.popup_window_button).text = popupButton

        // Set the Status bar appearance for different API levels
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }


        // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            findViewById<ConstraintLayout>(R.id.popup_window_background).setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()


        // Fade animation for the Popup Window
        findViewById<CardView>(R.id.popup_window_view_with_border).alpha = 0f
        findViewById<CardView>(R.id.popup_window_view_with_border).animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // Close the Popup Window when you press the button
        findViewById<Button>(R.id.popup_window_button).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }


    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            findViewById<ConstraintLayout>(R.id.popup_window_background).setBackgroundColor(animator.animatedValue as Int)
        }

        // Fade animation for the Popup Window when you press the back button
        findViewById<CardView>(R.id.popup_window_view_with_border).animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()
        val context = this
        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (intent.extras?.getBoolean("tokenValidation") == true) {
                    val intent = Intent(context, MainLandingPage::class.java)
                    startActivity(intent)
                }
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }

}