package com.example.push.emotion.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.emotion.data.model.EmotionRecordRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionRecordScreen(
    viewModel: EmotionViewModel,
    emotionId: Int,
    onRecordSaved: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // Definición de colores consistentes con otras pantallas
    val primaryGreen = Color(45, 105, 24)
    val accentGreen = Color(139, 209, 10)
    val buttonGreen = Color(198, 241, 119)
    val backgroundColor = Color(18, 18, 18) // Dark theme background

    // Colores para diferentes intensidades
    val lowColor = Color(76, 175, 80).copy(alpha = 0.7f)
    val mediumColor = Color(255, 152, 0).copy(alpha = 0.7f)
    val highColor = Color(244, 67, 54).copy(alpha = 0.7f)

    val context = LocalContext.current
    var note by remember { mutableStateOf("") }
    var intensity by remember { mutableStateOf("MEDIUM") }
    val postSuccess by viewModel.postSuccess.observeAsState()
    val scope = rememberCoroutineScope()

    // Información de la emoción seleccionada
    val emotions by viewModel.emotions.observeAsState(emptyList())
    val selectedEmotion = emotions.find { it.id == emotionId }

    // Cargar emociones si no están cargadas aún
    LaunchedEffect(Unit) {
        if (emotions.isEmpty()) {
            viewModel.loadEmotions()
        }
    }

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
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Record Emotion",
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
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Mostrar la emoción seleccionada
                    selectedEmotion?.let { emotion ->
                        val emotionColor = try {
                            Color(android.graphics.Color.parseColor(emotion.color))
                        } catch (e: Exception) {
                            Color.Gray
                        }

                        // Círculo de la emoción
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(emotionColor),
                            contentAlignment = Alignment.Center
                        ) {
                            // Aquí podría ir un icono si tuvieras uno
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = emotion.name,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        emotion.description?.let { desc ->
                            Text(
                                text = desc,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    } ?: run {
                        // Si la emoción no se ha cargado aún
                        CircularProgressIndicator(color = accentGreen)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Selector de intensidad
                    Text(
                        text = "How intense is this emotion?",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botones de intensidad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IntensityButton(
                            text = "LOW",
                            isSelected = intensity == "LOW",
                            color = lowColor,
                            onClick = { intensity = "LOW" }
                        )

                        IntensityButton(
                            text = "MEDIUM",
                            isSelected = intensity == "MEDIUM",
                            color = mediumColor,
                            onClick = { intensity = "MEDIUM" }
                        )

                        IntensityButton(
                            text = "HIGH",
                            isSelected = intensity == "HIGH",
                            color = highColor,
                            onClick = { intensity = "HIGH" }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo para nota
                    Text(
                        text = "Add a note about how you feel",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("What's on your mind?", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentGreen,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = accentGreen,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón para guardar
                    Button(
                        onClick = {
                            if (note.isNotBlank()) {
                                scope.launch {
                                    val request = EmotionRecordRequest(
                                        emotion_id = emotionId,
                                        note = note,
                                        intensity = intensity
                                    )
                                    viewModel.createRecordEmotion(request)
                                }
                            } else {
                                Toast.makeText(context, "Please add a note before saving", Toast.LENGTH_SHORT).show()
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
                            text = "Save Record",
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
fun IntensityButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) color else Color.White.copy(alpha = 0.1f),
            contentColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(90.dp)
    ) {
        Text(
            text = text,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
