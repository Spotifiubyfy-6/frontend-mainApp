package com.example.spotifiubyfy01

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.spotifiubyfy01.Messages.ChatPage
import com.example.spotifiubyfy01.search.Artist

class NotificationCreator {

    fun setChannels() {
        /*val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val channel = NotificationChannel(CHANNEL_ID, "Heads Up Notification",
            NotificationManager.IMPORTANCE_HIGH)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)*/
    }

    fun createNotificationWithIntent(context: Context, intent: Intent) {
        val message = intent.extras!!.get("message") as String
        val dateString = intent.extras!!.get("date") as String
        val idSender = (intent.extras!!.get("idSender") as String).toInt()
        val artistName = intent.extras!!.get("name") as String
        val image = intent.extras!!.get("image") as String
        val artist = Artist(idSender, artistName, image)
        val userId = intent.extras!!.get("idUser") as Int
        val myIntent = Intent(context, ChatPage::class.java)
        myIntent.putExtra("other", artist)
        myIntent.putExtra("requester_id", userId)
        val pendingIntent = PendingIntent.getActivity(context,0, myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val notification = Notification.Builder(context, CHANNEL_ID)
            .setContentTitle("New message from: " + artistName).setContentText(message)
            .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true)
            .setContentIntent(pendingIntent)
        NotificationManagerCompat.from(context).notify(1, notification.build())
    }
}