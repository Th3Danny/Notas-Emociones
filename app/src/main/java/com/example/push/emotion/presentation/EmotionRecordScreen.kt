package com.example.push.emotion.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.push.emotion.data.model.EmotionRecordRequest

@Composable
fun EmotionRecordScreen(
    viewModel: EmotionViewModel,
    emotionId: Int,
    onRecordSaved: () -> Unit
) {
    val context = LocalContext.current
    var note by remember { mutableStateOf("") }
    var intensity by remember { mutableStateOf("MEDIUM") }
    val postSuccess by viewModel.postSuccess.observeAsState()

    LaunchedEffect(postSuccess) {
        when (postSuccess) {
            true -> {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                onRecordSaved()
                viewModel.resetStatus()
            }
            false -> {
                Toast.makeText(context, "Error al registrar emoción", Toast.LENGTH_SHORT).show()
                viewModel.resetStatus()
            }
            null -> {
                // No hacer nada
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Nota") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Text("Intensidad", style = MaterialTheme.typography.labelLarge)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("LOW", "MEDIUM", "HIGH").forEach {
                Button(
                    onClick = { intensity = it },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (intensity == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(it)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (note.isNotBlank()) {
                    val request = EmotionRecordRequest(
                        emotion_id = emotionId,
                        note = note,
                        intensity = intensity
                    )
                    viewModel.createRecordEmotion(request)
                } else {
                    Toast.makeText(context, "Escribe una nota antes de guardar", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar emoción")
        }
    }
}
