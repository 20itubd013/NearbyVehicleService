package com.nearbyvechicleservice.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nearbyvechicleservice.local.PreferenceManager

class RequestService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("token",token)
        PreferenceManager(this).fcmToken = token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("datassss", "From" + remoteMessage.data)
    }

}