package com.example.spotifiubyfy01

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()){
            val title = "Message from " + (remoteMessage.data["name"] as String)
            val message = remoteMessage.data["message"] as String
            val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
            val channel = NotificationChannel(CHANNEL_ID, "Heads Up Notification",
                                                 NotificationManager.IMPORTANCE_HIGH)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
            val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title).setContentText(message).setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true)
            NotificationManagerCompat.from(this).notify(1, notification.build())
            Log.d("TAG", "Message data payload: " + remoteMessage.data);
        }
        super.onMessageReceived(remoteMessage)
    }
}