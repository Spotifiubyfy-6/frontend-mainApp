package com.example.spotifiubyfy01

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.spotifiubyfy01.Messages.ChatPage
import com.example.spotifiubyfy01.Messages.Message
import com.example.spotifiubyfy01.Messages.MessagesDataSource.Companion.obtainDate
import com.example.spotifiubyfy01.search.Artist
import java.time.LocalDateTime


const val CURRENT_ACTIVITY_ACTION = "current.activity.action";
val CURRENT_ACTIVITY_RECEIVER_FILTER = IntentFilter(CURRENT_ACTIVITY_ACTION);



class CurrentActivityReceiver(private val receivingActivity: Activity): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "class " + getClassName(receivingActivity) + " is running!")
        //val userId = (receivingActivity.application as Spotifiubify).getProfileData("id")!!.toInt()
        //intent.putExtra("user")
        if (getClassName(receivingActivity) == "ChatPage") {
            updateChat(intent)
            return
        }
        NotificationCreator().createNotificationWithIntent(context, intent)
    }

    private fun updateChat(intent: Intent) {
        val userId = intent.extras!!.get("idUser") as Int
        val message = intent.extras!!.get("message") as String
        val dateString = intent.extras!!.get("date") as String
        val date = obtainDateWithoutT(dateString)
        val messageReceived = Message(userId, userId, message, date)
        (receivingActivity as ChatPage).addMessage(messageReceived, date)
    }

    private fun obtainDateWithoutT(dateString: String): LocalDateTime {
        val year = dateString.substringBefore("-")
        val month = dateString.substringAfter("$year-").substringBefore("-")
        val day = dateString.substringAfter("$year-$month-").substringBefore(" ")
        val hour = dateString.substringAfter("$year-$month-$day ").substringBefore(":")
        val minute = dateString.substringAfter(" $hour:").substringBefore(":")
        return LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())
    }

    private fun getClassName(activity: Activity): String {
        return activity.localClassName.substringAfterLast('.')
    }
}

open class NotificationReceiverActivity : AppCompatActivity() {
    private var currentActivityReceiver: BroadcastReceiver? = null

    override fun onResume() {
        super.onResume()
        (this.application as Spotifiubify).activityResumed()
        currentActivityReceiver = CurrentActivityReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            currentActivityReceiver!!,
            CURRENT_ACTIVITY_RECEIVER_FILTER
        )
    }

    override fun onPause() {
        (this.application as Spotifiubify).activityPaused()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(currentActivityReceiver!!)
        currentActivityReceiver = null
        super.onPause()
    }
}