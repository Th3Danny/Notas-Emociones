package com.example.push.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.example.push.worker.scheduleNoteSync

object NetworkMonitor {

    fun registerNetworkCallback(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val request = NetworkRequest.Builder().build()

        connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // 🚨 Se detectó conexión a internet
                scheduleNoteSync(context)
            }

            override fun onLost(network: Network) {
                // Aquí podrías manejar la pérdida si lo deseas
            }
        })
    }
}