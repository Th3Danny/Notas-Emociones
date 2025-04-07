package com.example.push

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import androidx.core.content.ContextCompat
import com.example.push.core.navigation.NavigationWrapper
import com.example.push.core.network.NetworkMonitor
import com.example.push.core.network.RetrofitHelper
import com.example.push.core.services.scheduleDailyNotification
import com.example.push.core.session.SessionManager
import com.example.push.ui.theme.PushTheme

class MainActivity : ComponentActivity() {

    // Registro del launcher para solicitar permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, podemos programar notificaciones
            scheduleDailyNotification(applicationContext)
        } else {
            // Permiso denegado, podríamos mostrar un mensaje explicando
            // por qué necesitamos el permiso
            Toast.makeText(
                this,
                "Las notificaciones son necesarias para recordarte registrar tus emociones",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkMonitor.registerNetworkCallback(applicationContext)
        enableEdgeToEdge()

        // Inicializar RetrofitHelper con SessionManager
        val sessionManager = SessionManager(applicationContext)
        RetrofitHelper.initialize(sessionManager)

        // Solicitar permisos de notificación antes de programarlas
        askNotificationPermission()

        setContent {
            PushTheme {
                NavigationWrapper()
            }
        }
    }

    private fun askNotificationPermission() {
        // Verificar si necesitamos solicitar el permiso (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Solicitar el permiso
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Permiso ya concedido, programar notificaciones
                scheduleDailyNotification(applicationContext)
            }
        } else {
            // Para versiones de Android anteriores a la 13, no necesitamos solicitar este permiso
            scheduleDailyNotification(applicationContext)
        }
    }
}