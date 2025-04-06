package com.example.push.core.services

import com.google.firebase.messaging.FirebaseMessagingService
import android.util.Log
import android.content.Context
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM", "Token actualizado: $token")

        // Guardar en SharedPreferences
        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("fcm_token", token).apply()

        // Opcional: Enviar al servidor
        // sendTokenToBackend(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Aquí puedes manejar notificaciones personalizadas si deseas
        Log.d("FCM", "Notificación recibida: ${message.notification?.body}")
    }
}