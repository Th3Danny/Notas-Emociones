package com.example.push.home.presentation


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.core.navigation.AppRoutes
import com.example.push.emotion.presentation.EmotionViewModel

@Composable
fun HomeUI(
    navigateToNotes: () -> Unit,
    navigateToNewEmotion: () -> Unit,
    navigateToRecordEmotion: (Int) -> Unit,
    navigateToStatistics: () -> Unit,
    emotionViewModel: EmotionViewModel,
    onLogout: () -> Unit
){
    // Definición de colores consistentes con las otras pantallas
    val primaryGreen = Color(45, 105, 24)
    val accentGreen = Color(139, 209, 10)
    val buttonGreen = Color(198, 241, 119)
    val backgroundColor = Color(18, 18, 18) // Dark theme background

    // Obtener emociones del ViewModel
    val emotions by emotionViewModel.emotions.observeAsState(emptyList())

    // Cargar emociones al iniciar
    LaunchedEffect(Unit) {
        Log.d("HomeUI", "Cargando emociones...")
        emotionViewModel.loadEmotions()
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
            // Header con título de la app
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lumimood",
                    color = accentGreen,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = { onLogout() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD95C5C).copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "Logout",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navigateToStatistics() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "View Weekly Statistics",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }


            // Sección de bienvenida y estado de ánimo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "How are you feeling today?",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Usar las emociones del backend
                        items(emotions.size) { index ->
                            val emotion = emotions[index]

                            // Convertir el color hexadecimal a Color
                            val emotionColor = try {
                                Color(android.graphics.Color.parseColor(emotion.color))
                            } catch (e: Exception) {
                                // Color por defecto si hay error
                                Color.Gray
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { navigateToRecordEmotion(emotion.id) }
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(emotionColor)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = emotion.name,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Sección de acciones principales
            Text(
                text = "What would you like to do?",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .clickable { navigateToNotes() },
                    colors = CardDefaults.cardColors(
                        containerColor = buttonGreen.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icono para notas
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Create Note",
                            tint = primaryGreen,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Create Note",
                            color = primaryGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = "Write down your thoughts",
                            color = primaryGreen.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Botón adicional para registrar nueva emoción
            Button(
                onClick = { navigateToNewEmotion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Register New Emotion",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Espacio para contenido adicional
            Spacer(modifier = Modifier.weight(1f))

            // Footer con información de la app
            Text(
                text = "Track your emotions and notes daily",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}