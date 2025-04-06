package com.example.push

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.push.core.navigation.NavigationWrapper
import com.example.push.core.network.NetworkMonitor
import com.example.push.core.network.RetrofitHelper
import com.example.push.core.services.scheduleDailyNotification
import com.example.push.core.session.SessionManager
import com.example.push.ui.theme.PushTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkMonitor.registerNetworkCallback(applicationContext)
        enableEdgeToEdge()

        // Inicializar RetrofitHelper con SessionManager
        val sessionManager = SessionManager(applicationContext)
        RetrofitHelper.initialize(sessionManager)

        // Programar notificaciones diarias al iniciar la app
        scheduleDailyNotification(applicationContext)
        setContent {
            PushTheme {
                NavigationWrapper()
            }
        }
    }
}