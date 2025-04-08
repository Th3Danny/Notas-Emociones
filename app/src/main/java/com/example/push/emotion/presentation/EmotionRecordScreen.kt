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
    // Colores definidos en el HomeUI
    val primaryGreen = Color(45, 105, 24)  // Verde oscuro (para textos importantes)
    val accentGreen = Color(139, 209, 10)  // Verde brillante (para acentos)
    val buttonGreen = Color(198, 241, 119) // Verde claro (para botones)
    val backgroundColor = Color.White      // Fondo blanco
    val textColor = Color(60, 60, 60)      // Texto oscuro para mejor legibilidad
    val cardBackground = Color(250, 250, 250) // Gris muy claro para tarjetas

    // Colores para diferentes intensidades con menos saturación
    val lowColor = Color(76, 175, 80).copy(alpha = 0.7f)   // Verde menos intenso
    val mediumColor = Color(255, 152, 0).copy(alpha = 0.7f) // Naranja menos intenso
    val highColor = Color(244, 67, 54).copy(alpha = 0.7f)   // Rojo menos intenso

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
            .background(backgroundColor)
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
                        tint = primaryGreen
                    )
                }

                Text(
                    text = "Record Emotion",
                    color = textColor,
                    fontSize = 22.sp,
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
                    containerColor = cardBackground
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
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
                            color = textColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        emotion.description?.let { desc ->
                            Text(
                                text = desc,
                                color = Color.Gray,
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
                        color = textColor,
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
                        Button(
                            onClick = { intensity = "LOW" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (intensity == "LOW") lowColor else Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(90.dp)
                        ) {
                            Text(
                                text = "LOW",
                                color = if (intensity == "LOW") Color.White else Color.DarkGray
                            )
                        }

                        Button(
                            onClick = { intensity = "MEDIUM" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (intensity == "MEDIUM") mediumColor else Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(90.dp)
                        ) {
                            Text(
                                text = "MEDIUM",
                                color = if (intensity == "MEDIUM") Color.White else Color.DarkGray
                            )
                        }

                        Button(
                            onClick = { intensity = "HIGH" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (intensity == "HIGH") highColor else Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(90.dp)
                        ) {
                            Text(
                                text = "HIGH",
                                color = if (intensity == "HIGH") Color.White else Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo para nota
                    Text(
                        text = "Add a note about how you feel",
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("What's on your mind?", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentGreen,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = accentGreen,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = textColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
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
                            containerColor = accentGreen
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Save Record",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

