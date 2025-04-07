package com.example.push.emotion.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.emotion.data.model.NewEmotionRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEmotionScreen(
    emotionViewModel: EmotionViewModel,
    onEmotionCreated: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // Definición de colores consistentes con las otras pantallas
    val primaryGreen = Color(45, 105, 24)
    val accentGreen = Color(139, 209, 10)
    val buttonGreen = Color(198, 241, 119)
    val backgroundColor = Color(18, 18, 18) // Dark theme background

    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("#4CAF50") } // Color verde por defecto
    var icon by remember { mutableStateOf("emoji-smile") } // Icono por defecto
    val postSuccess by emotionViewModel.postSuccess.observeAsState()

    // Lista de colores predefinidos para elegir
    val predefinedColors = listOf(
        "#4CAF50", // Verde
        "#2196F3", // Azul
        "#FFC107", // Amarillo
        "#F44336", // Rojo
        "#9C27B0", // Púrpura
        "#FF9800", // Naranja
        "#607D8B", // Gris azulado
        "#E91E63", // Rosa
        "#009688", // Verde azulado
        "#673AB7"  // Violeta
    )

    // Lista de iconos predefinidos
    val predefinedIcons = listOf(
        "emoji-smile",
        "emoji-frown",
        "emoji-angry",
        "emoji-dizzy",
        "emoji-fearful",
        "emoji-neutral",
        "emoji-surprised",
        "emoji-tired",
        "emoji-smile-upside-down"
    )

    LaunchedEffect(postSuccess) {
        when (postSuccess) {
            true -> {
                Toast.makeText(context, "Emoción creada con éxito", Toast.LENGTH_SHORT).show()
                onEmotionCreated()
            }
            false -> {
                Toast.makeText(context, "Error al crear emoción", Toast.LENGTH_SHORT).show()
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
                    text = "Create New Emotion",
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
                    // Vista previa de la emoción
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(
                                try {
                                    Color(android.graphics.Color.parseColor(color))
                                } catch (e: Exception) {
                                    Color.Gray
                                }
                            )
                            .border(
                                width = 2.dp,
                                color = Color.White.copy(alpha = 0.3f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campos de entrada
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Emotion Name", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // Selector de color
                    Text(
                        text = "Choose a Color",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo para ingresar código hexadecimal
                    OutlinedTextField(
                        value = color,
                        onValueChange = { newColor ->
                            // Restringir a formato hexadecimal
                            if (newColor.startsWith("#") && newColor.length <= 7) {
                                color = newColor
                            }
                        },
                        label = { Text("Hexadecimal Color Code", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentGreen,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = accentGreen,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        try {
                                            Color(android.graphics.Color.parseColor(color))
                                        } catch (e: Exception) {
                                            Color.Gray
                                        }
                                    )
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Quick Colors",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        predefinedColors.forEach { predefinedColor ->
                            val isSelected = color == predefinedColor
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        try {
                                            Color(android.graphics.Color.parseColor(predefinedColor))
                                        } catch (e: Exception) {
                                            Color.Gray
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { color = predefinedColor }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Selector de icono simplificado
                    Text(
                        text = "Choose an Icon",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mostrar los iconos disponibles (simplificado para este ejemplo)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(predefinedIcons.size) { index ->
                            val iconName = predefinedIcons[index]
                            val isSelected = icon == iconName

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { icon = iconName }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) accentGreen else Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = iconName,
                                        tint = if (isSelected) Color.Black else Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = iconName.split("-").last().take(5),
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón para guardar
                    Button(
                        onClick = {
                            if (name.isNotBlank() && description.isNotBlank()) {
                                emotionViewModel.createEmotion(
                                    NewEmotionRequest(name, description, color, icon)
                                )
                            } else {
                                Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
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
                            text = "Save Emotion",
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