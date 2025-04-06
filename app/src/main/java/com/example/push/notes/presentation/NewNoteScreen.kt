package com.example.push.notes.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.emotion.presentation.EmotionViewModel
import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.model.NoteRequest

@Composable
fun NewNoteScreen(
    noteViewModel: NoteViewModel,
    emotionViewModel: EmotionViewModel,
    onNoteCreated: () -> Unit
) {
    val context = LocalContext.current

    var content by remember { mutableStateOf("") }
    var selectedEmotionId by remember { mutableStateOf<Int?>(null) }

    val postSuccess by noteViewModel.postSuccess.observeAsState()
    val emotions by emotionViewModel.emotions.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        Log.d("NewNoteScreen", "Llamando a emotionViewModel.loadEmotions()")
        emotionViewModel.loadEmotions()
    }


    LaunchedEffect(postSuccess) {
        if (postSuccess == true) {
            Toast.makeText(context, "Nota guardada correctamente", Toast.LENGTH_SHORT).show()
            onNoteCreated()
        } else if (postSuccess == false) {
            Toast.makeText(context, "Error al guardar nota", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(235, 235, 235))
            .padding(24.dp)
    ) {
        Text("Nueva Nota", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Contenido de la nota") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        EmotionDropdown(
            emotions = emotions,
            selectedEmotionId = selectedEmotionId,
            onSelectEmotion = { selectedEmotionId = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (content.isNotBlank() && selectedEmotionId != null) {
                    val request = NewNoteRequest(content, selectedEmotionId!!)
                    noteViewModel.createNote(context, request)
                } else {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A))
        ) {
            Text("Guardar nota", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

    }
}

@Composable
fun EmotionDropdown(
    emotions: List<EmotionResponse>,
    selectedEmotionId: Int?,
    onSelectEmotion: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(emotions.find { it.id == selectedEmotionId }?.name ?: "Selecciona una emoción")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            emotions.forEach { emotion ->
                DropdownMenuItem(
                    text = { Text(emotion.name) },
                    onClick = {
                        onSelectEmotion(emotion.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
