package com.example.spotifiubyfy01

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.isNotEmpty()){
            val app = (this.application as Spotifiubify)
            val title = remoteMessage.data["title"] as String
            if (title == "song") {
                NotificationCreator().createNotificationForNewSongWithRemoteMessage(this, remoteMessage)
            } else if (title == "message") {
                val intent = createIntentForNewMessageNotification(remoteMessage, app.getProfileData("id")!!.toInt())
                if (app.isActivityVisible())
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                else
                    NotificationCreator().createNotificationForNewMessageWithIntent(this, intent)
            } else if (title == "followers") {
                NotificationCreator().createNotificationForFollowersMilestoneWithRemoteMessage(this, remoteMessage)
            }
        }
        super.onMessageReceived(remoteMessage)
    }

    private fun createIntentForNewMessageNotification(remoteMessage: RemoteMessage, userId: Int): Intent {
        val idSender = remoteMessage.data["idSender"] as String
        val message = remoteMessage.data["message"] as String
        val name = remoteMessage.data["name"] as String
        val image = remoteMessage.data["image"] as String
        val date = remoteMessage.data["time"] as String
        val localMessage = Intent(CURRENT_ACTIVITY_ACTION)
        localMessage.putExtra("idUser", userId)
        localMessage.putExtra("idSender", idSender)
        localMessage.putExtra("message", message)
        localMessage.putExtra("name", name)
        localMessage.putExtra("image", image)
        localMessage.putExtra("date", date)
        return localMessage
    }
}