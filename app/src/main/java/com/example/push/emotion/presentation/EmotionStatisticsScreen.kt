package com.example.push.emotion.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EmotionStatisticsScreen(
    viewModel: EmotionViewModel,
    onNavigateBack: () -> Unit = {}
) {
    // Colores definidos en el HomeUI
    val primaryGreen = Color(45, 105, 24)  // Verde oscuro (para textos importantes)
    val accentGreen = Color(139, 209, 10)  // Verde brillante (para acentos)
    val buttonGreen = Color(198, 241, 119) // Verde claro (para botones)
    val backgroundColor = Color.White      // Fondo blanco
    val textColor = Color(60, 60, 60)      // Texto oscuro para mejor legibilidad
    val cardBackground = Color(250, 250, 250) // Gris muy claro para tarjetas

    // Estado de datos
    val stats by viewModel.weeklyStats.observeAsState()
    val scrollState = rememberScrollState()

    // Cargar datos
    LaunchedEffect(Unit) {
        viewModel.loadWeeklyStats()
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
                .verticalScroll(scrollState)
        ) {
            // Header con título y botón de retroceso
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
                    text = "Weekly Statistics",
                    color = textColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                // Espacio para equilibrar el layout
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Resumen de emociones
            stats?.data?.emotion_counts?.let { counts ->
                if (counts.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
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
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Emotion Summary",
                                color = textColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Círculos de colores para cada emoción con su conteo
                            counts.entries.forEach { (emotionName, count) ->
                                val colorHex = stats?.data?.emotion_colors?.get(emotionName) ?: "#CCCCCC"
                                val emotionColor = try {
                                    Color(android.graphics.Color.parseColor(colorHex))
                                } catch (e: Exception) {
                                    Color.Gray
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(emotionColor)
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = emotionName,
                                        color = textColor,
                                        fontSize = 16.sp,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = count.toString(),
                                        color = textColor,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (emotionName != counts.keys.last()) {
                                    Divider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Emociones diarias
            Text(
                text = "Daily Records",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            stats?.data?.daily_emotions?.let { dailyEmotions ->
                dailyEmotions.forEach { day ->
                    val date = try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
                        val parsedDate = inputFormat.parse(day.date)
                        outputFormat.format(parsedDate)
                    } catch (e: Exception) {
                        day.date
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
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
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = date,
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (day.emotions.isEmpty()) {
                                Text(
                                    text = "No emotions recorded",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            } else {
                                day.emotions.forEach { (emotionName, count) ->
                                    val colorHex = stats?.data?.emotion_colors?.get(emotionName) ?: "#CCCCCC"
                                    val emotionColor = try {
                                        Color(android.graphics.Color.parseColor(colorHex))
                                    } catch (e: Exception) {
                                        Color.Gray
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clip(CircleShape)
                                                .background(emotionColor)
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Text(
                                            text = emotionName,
                                            color = textColor,
                                            fontSize = 15.sp,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(emotionColor.copy(alpha = 0.15f))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "×$count",
                                                color = emotionColor,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } ?: run {
                // Si no hay datos, mostrar mensaje
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
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
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = accentGreen,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Loading emotion statistics...",
                            color = textColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
