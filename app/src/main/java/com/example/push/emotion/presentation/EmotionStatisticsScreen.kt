package com.example.push.emotion.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmotionStatisticsScreen(viewModel: EmotionViewModel) {
    val stats by viewModel.weeklyStats.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeeklyStats()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Registro de Emociones Semanal", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        stats?.data?.daily_emotions?.forEach { day ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📅 Fecha: ${day.date}", fontWeight = FontWeight.SemiBold)

                    if (day.emotions.isEmpty()) {
                        Text("Sin emociones registradas", color = Color.Gray)
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        day.emotions.forEach { (emotionName, count) ->
                            val colorHex = stats?.data?.emotion_colors?.get(emotionName) ?: "#CCCCCC"
                            val colorParsed = try {
                                Color(android.graphics.Color.parseColor(colorHex))
                            } catch (_: Exception) {
                                Color.Gray
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(colorParsed, shape = CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$emotionName: $count")
                            }
                        }
                    }
                }
            }
        }
    }
}
