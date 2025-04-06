package com.example.push.eotw.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class MoodEntry(val date: LocalDate)

@Composable
fun EmotionsOfTheWeek(moodEntries: List<MoodEntry>) {
    Column(
        modifier = Modifier
            .background(Color(235, 235, 235))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Emotions of the Week",
            color = Color(45, 105, 24),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(moodEntries) { mood ->
                MoodItem(mood)
            }
        }
    }
}

@Composable
fun MoodItem(entry: MoodEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                //  text = entry.date.format(DateTimeFormatter.ofPattern("EEEE")),
                text= "",
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun PreviewEmotionsOfTheWeek() {
   // val sampleMoods = listOf(
        //MoodEntry(LocalDate.now().minusDays(2)),
        //oodEntry(LocalDate.now().minusDays(1)),
       // MoodEntry(LocalDate.now())
    //)
   // EmotionsOfTheWeek(sampleMoods)
}

