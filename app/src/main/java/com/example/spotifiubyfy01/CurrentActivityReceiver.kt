package com.example.spotifiubyfy01

import android.app.Activity
import android.app.NotificationChannel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.app.Notification
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat

const val CURRENT_ACTIVITY_ACTION = "current.activity.action";
val CURRENT_ACTIVITY_RECEIVER_FILTER = IntentFilter(CURRENT_ACTIVITY_ACTION);

class CurrentActivityReceiver(private val receivingActivity: Activity): BroadcastReceiver() {
    override fun onReceive(sender: Context, intent: Intent) {
        val title = intent.extras!!.get("title") as String
        val message = intent.extras!!.get("title") as String

       /* val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val channel = NotificationChannel(CHANNEL_ID, "Heads Up Notification",
                                     NotificationManager.IMPORTANCE_HIGH)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        */
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val notification = Notification.Builder(receivingActivity, CHANNEL_ID)
            .setContentTitle(title).setContentText(message).setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true)
        NotificationManagerCompat.from(receivingActivity).notify(1, notification.build())
    }
}  

open class NotificationReceiverActivity : AppCompatActivity() {
    private var currentActivityReceiver: BroadcastReceiver? = null

    override fun onResume() {
        super.onResume()
        currentActivityReceiver = CurrentActivityReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            currentActivityReceiver!!,
            CURRENT_ACTIVITY_RECEIVER_FILTER
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(currentActivityReceiver!!)
        currentActivityReceiver = null
        super.onPause()
    }
}