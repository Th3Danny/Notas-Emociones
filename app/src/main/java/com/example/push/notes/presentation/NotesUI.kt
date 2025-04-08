package com.example.push.notes.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.notes.data.model.NoteResponse


@Composable
fun NotesUI(
    noteViewModel: NoteViewModel,
    navigateToNewNote: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // Colores para el tema claro
    val primaryGreen = Color(45, 105, 24)  // Verde oscuro (para textos importantes)
    val accentGreen = Color(139, 209, 10)  // Verde brillante (para acentos)
    val buttonGreen = Color(198, 241, 119) // Verde claro (para botones)
    val backgroundColor = Color.White      // Fondo blanco
    val textColor = Color(60, 60, 60)      // Texto oscuro para mejor legibilidad
    val cardBackground = Color(250, 250, 250) // Gris muy claro para tarjetas

    val notes by noteViewModel.notes.observeAsState(emptyList())

    // Llamar a getNotes al iniciar la pantalla
    LaunchedEffect(Unit) {
        Log.d("NotesUI", "Llamando a noteViewModel.getNotes()")
        noteViewModel.getNotes()
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
                    text = "Your Notes",
                    color = textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Espacio para equilibrar el layout
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de notas
            if (notes.isEmpty()) {
                // Mensaje cuando no hay notas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notes yet. Create your first note!",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                // Lista de notas existentes
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(notes) { note ->
                        NoteCard(note = note, textColor = textColor, cardBackground = cardBackground)
                    }
                }
            }
        }

        // Botón flotante para añadir nota
        FloatingActionButton(
            onClick = { navigateToNewNote() },
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomEnd),
            containerColor = accentGreen,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Note"
            )
        }
    }
}

@Composable
fun NoteCard(
    note: NoteResponse,
    textColor: Color,
    cardBackground: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Contenido de la nota
            Text(
                text = note.content,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
        }
    }
}