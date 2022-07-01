package com.example.spotifiubyfy01

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()){
            val title = "Message from " + (remoteMessage.data["name"] as String)
            val message = remoteMessage.data["message"] as String
            val localMessage = Intent(CURRENT_ACTIVITY_ACTION)
            localMessage.putExtra("title", title)
            localMessage.putExtra("message", message)
            LocalBroadcastManager.getInstance(this).sendBroadcast(localMessage)
        }
        super.onMessageReceived(remoteMessage)
    }
}