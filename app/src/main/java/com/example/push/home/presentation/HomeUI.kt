package com.example.push.home.presentation

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeUI(
    navigateToNotes: () -> Unit,
    navigateToEmotion: () -> Unit,
    navigateToNewEmotion: () -> Unit,
    onLogout: () -> Unit
)

{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(235, 235, 235))
    ) {

        Text(
            text = "What do you want to do?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botones de navegación
        Button(
            onClick = { navigateToNotes() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2FF59))
        ) {
            Text(text = "Create Note", color = Color(0xFF1B5E20), fontSize = 18.sp)
        }

        Button(
            onClick = { navigateToNewEmotion() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA))
        ) {
            Text(text = "Register Emotion", color = Color(0xFF01579B), fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD95C5C))
        ) {
            Text("Cerrar sesión", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "What are you feeling today?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        LazyRow(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(6) { index ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {

                    val color = when (index) {
                        0 -> Color(189, 0, 0) // Anger
                        1 -> Color(233, 108, 25, 255) // Surprise
                        2 -> Color(201, 205, 10, 255) // Joy
                        3 -> Color(24, 192, 9, 255) // Sadness
                        4 -> Color(6, 102, 185, 255) // Love
                        5 -> Color(134, 13, 185, 255) // Fear
                        else -> Color.Gray
                    }


                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(color)
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    Text(
                        text = when (index) {
                            0 -> "Anger"
                            1 -> "Surprise"
                            2 -> "Happy"
                            3 -> "Disgust"
                            4 -> "Sad"
                            5 -> "Fear"
                            else -> "Unknown"
                        },
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
