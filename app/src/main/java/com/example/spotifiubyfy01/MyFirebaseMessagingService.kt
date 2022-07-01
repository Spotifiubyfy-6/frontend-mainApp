package com.example.spotifiubyfy01

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()){
            Log.d("TAG", "Message data payload: " + message.data);
        }
        Log.d("TAG", "notification received!")
    }
}