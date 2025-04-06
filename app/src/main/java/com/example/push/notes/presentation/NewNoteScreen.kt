package com.example.push.notes.presentation


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.emotion.presentation.EmotionViewModel
import com.example.push.notes.data.model.NewNoteRequest


@Composable
fun NewNoteScreen(
    noteViewModel: NoteViewModel,
    emotionViewModel: EmotionViewModel,
    onNoteCreated: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // Definición de colores consistentes con el resto de la app
    val primaryGreen = Color(45, 105, 24)
    val accentGreen = Color(139, 209, 10)
    val buttonGreen = Color(198, 241, 119)
    val backgroundColor = Color(18, 18, 18) // Dark theme background

    val context = LocalContext.current
    var content by remember { mutableStateOf("") }
    var selectedEmotionId by remember { mutableStateOf<Int?>(null) }

    val postSuccess by noteViewModel.postSuccess.observeAsState()
    val emotions by emotionViewModel.emotions.observeAsState(emptyList())

    // Cargar emociones
    LaunchedEffect(Unit) {
        Log.d("NewNoteScreen", "Llamando a emotionViewModel.loadEmotions()")
        emotionViewModel.loadEmotions()
    }

    // Manejar resultado de guardar nota
    LaunchedEffect(postSuccess) {
        when (postSuccess) {
            true -> {
                Toast.makeText(context, "Nota guardada con éxito", Toast.LENGTH_SHORT).show()
                content = ""
                selectedEmotionId = null
                noteViewModel.clearPostSuccess()
                onNoteCreated()
            }
            false -> {
                Toast.makeText(context, "Nota guardada localmente (sin conexión)", Toast.LENGTH_LONG).show()
                content = ""
                selectedEmotionId = null
                noteViewModel.clearPostSuccess()
            }
            null -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(backgroundColor, backgroundColor.copy(alpha = 0.8f)),
                    startY = 0f,
                    endY = 2000f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header con botón de retroceso
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigateBack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "New Note",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Espacio para equilibrar el layout
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Tarjeta principal para el formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Icono y título de la nota
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Create Note",
                            tint = accentGreen,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Create Your Note",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de texto para la nota
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Write your thoughts here...", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentGreen,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = accentGreen,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Dropdown para seleccionar emoción
                    Text(
                        text = "How are you feeling?",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    EmotionDropdown(
                        emotions = emotions,
                        selectedEmotionId = selectedEmotionId,
                        onSelectEmotion = { selectedEmotionId = it },
                        accentColor = accentGreen
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón para guardar
                    Button(
                        onClick = {
                            if (content.isNotBlank() && selectedEmotionId != null) {
                                val request = NewNoteRequest(content, selectedEmotionId!!)
                                Log.d("NewNoteScreen", "Creando nota: $request")
                                noteViewModel.createNote(context, request)
                            } else {
                                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonGreen
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Save Note",
                            color = primaryGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmotionDropdown(
    emotions: List<EmotionResponse>,
    selectedEmotionId: Int?,
    onSelectEmotion: (Int) -> Unit,
    accentColor: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedEmotion = emotions.find { it.id == selectedEmotionId }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                width = 1.dp
            )
        ) {
            Text(
                text = selectedEmotion?.name ?: "Select an emotion",
                color = if (selectedEmotion != null) accentColor else Color.White.copy(alpha = 0.7f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(30, 30, 30))
        ) {
            emotions.forEach { emotion ->
                DropdownMenuItem(
                    text = { Text(emotion.name, color = Color.White) },
                    onClick = {
                        onSelectEmotion(emotion.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
