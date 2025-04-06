package com.example.push.emotion.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.push.emotion.data.model.NewEmotionRequest

@Composable
fun NewEmotionScreen(
    emotionViewModel: EmotionViewModel,
    onEmotionCreated: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("#") }
    var icon by remember { mutableStateOf("") }
    val postSuccess by emotionViewModel.postSuccess.observeAsState()

    LaunchedEffect(postSuccess) {
        when (postSuccess) {
            true -> {
                Toast.makeText(context, "Emoción creada", Toast.LENGTH_SHORT).show()
                onEmotionCreated()
            }
            false -> {
                Toast.makeText(context, "Error al crear emoción", Toast.LENGTH_SHORT).show()
            }
            null -> {
                // No hacer nada (opcional)
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
        OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") })
        OutlinedTextField(value = icon, onValueChange = { icon = it }, label = { Text("Ícono") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            emotionViewModel.createEmotion(
                NewEmotionRequest(name, description, color, icon)
            )
        }) {
            Text("Guardar Emoción")
        }
    }
}