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


const val CURRENT_ACTIVITY_ACTION = "current.activity.action";
val CURRENT_ACTIVITY_RECEIVER_FILTER = IntentFilter(CURRENT_ACTIVITY_ACTION);



class CurrentActivityReceiver(private val receivingActivity: Activity): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "class " + getClassName(receivingActivity) + " is running!")
        val userId = (receivingActivity.application as Spotifiubify).getProfileData("id")!!.toInt()
        val message = intent.extras!!.get("message") as String
        val dateString = intent.extras!!.get("date") as String
        val date = obtainDate(dateString)
        if (getClassName(receivingActivity) == "ChatPage") {
            val messageReceived = Message(userId, userId, message, date)
            (receivingActivity as ChatPage).addMessage(messageReceived, date)
            return
        }
        val idSender = (intent.extras!!.get("idSender") as String).toInt()
        val artistName = intent.extras!!.get("name") as String
        val image = intent.extras!!.get("image") as String
        val artist = Artist(idSender, artistName, image)
        val myIntent = Intent(context, ChatPage::class.java)
        myIntent.putExtra("other", artist)
        myIntent.putExtra("requester_id", userId)
        val pendingIntent = PendingIntent.getActivity(context,0, myIntent,
                                                    FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

       /* val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val channel = NotificationChannel(CHANNEL_ID, "Heads Up Notification",
                                     NotificationManager.IMPORTANCE_HIGH)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        */
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val notification = Notification.Builder(receivingActivity, CHANNEL_ID)
            .setContentTitle("New message from: " + artistName).setContentText(message)
            .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true)
            .setContentIntent(pendingIntent)
        NotificationManagerCompat.from(receivingActivity).notify(1, notification.build())
    }

    private fun getClassName(activity: Activity): String {
        return activity.localClassName.substringAfterLast('.')
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