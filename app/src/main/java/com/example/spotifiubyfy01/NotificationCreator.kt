package com.example.spotifiubyfy01

import android.app.*
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.spotifiubyfy01.Messages.ChatPage
import com.example.spotifiubyfy01.artistProfile.ArtistPage
import com.example.spotifiubyfy01.search.Artist
import com.google.firebase.messaging.RemoteMessage

class NotificationCreator {
    val messageNotification = 1
    val newSongNotification = 2
    val newFollowersNotification = 3

    fun setChannels(context: Context) {
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val channel = NotificationChannel(CHANNEL_ID, "Heads Up Notification",
            NotificationManager.IMPORTANCE_HIGH)
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
    }

    fun createNotificationForNewMessageWithIntent(context: Context, intent: Intent) {
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
        myIntent.putExtra("fromNotifications", true)
        val pendingIntent = PendingIntent.getActivity(context,0, myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val notification = Notification.Builder(context, CHANNEL_ID)
            .setContentTitle("New message from: " + artistName).setContentText(message)
            .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
        NotificationManagerCompat.from(context).notify(messageNotification, notification.build())
    }

    fun createNotificationForNewSongWithRemoteMessage(context: Context, remoteMessage: RemoteMessage) {
        val idSender = (remoteMessage.data["idSender"] as String).toInt()
        val name = remoteMessage.data["name"] as String
        val image = remoteMessage.data["image"] as String
        val songName = remoteMessage.data["song"] as String

        val myIntent = Intent(context, ArtistPage::class.java)
        val artist = Artist(idSender, name, image)
        myIntent.putExtra("Artist", artist)
        val pendingIntent = PendingIntent.getActivity(context,0, myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val notification = Notification.Builder(context, CHANNEL_ID)
            .setContentTitle("New song from: " + name).setContentText("Listen to " + songName + " now!")
            .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
        NotificationManagerCompat.from(context).notify(newSongNotification, notification.build())
    }

    fun createNotificationForFollowersMilestoneWithRemoteMessage(context: Context, remoteMessage: RemoteMessage) {
        val idSender = (remoteMessage.data["idSender"] as String).toInt()
        val name = remoteMessage.data["name"] as String
        val image = remoteMessage.data["image"] as String
        val followers = remoteMessage.data["followers"] as String

        val myIntent = Intent(context, ProfilePage::class.java)
        val artist = Artist(idSender, name, image)
        myIntent.putExtra("Artist", artist)
        val pendingIntent = PendingIntent.getActivity(context,0, myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
        val notification = Notification.Builder(context, CHANNEL_ID)
            .setContentTitle("Congratulations, " + name).setContentText("You've hit the milestone of " + followers + " followers!")
            .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
        NotificationManagerCompat.from(context).notify(newFollowersNotification, notification.build())
    }
}