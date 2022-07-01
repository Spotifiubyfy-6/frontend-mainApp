package com.example.spotifiubyfy01

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()){
            val idSender = remoteMessage.data["idSender"] as String
            val message = remoteMessage.data["message"] as String
            val name = remoteMessage.data["name"] as String
            val image = remoteMessage.data["image"] as String
            val date = remoteMessage.data["time"] as String
            Log.d("TAG", date)
            val localMessage = Intent(CURRENT_ACTIVITY_ACTION)
            localMessage.putExtra("idSender", idSender)
            localMessage.putExtra("message", message)
            localMessage.putExtra("name", name)
            localMessage.putExtra("image", image)
            localMessage.putExtra("date", date)
            LocalBroadcastManager.getInstance(this).sendBroadcast(localMessage)
        }
        super.onMessageReceived(remoteMessage)
    }
}