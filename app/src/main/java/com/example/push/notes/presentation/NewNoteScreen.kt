package com.example.push.notes.presentation


import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
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
    // Colores para el tema claro
    val primaryGreen = Color(45, 105, 24)  // Verde oscuro (para textos importantes)
    val accentGreen = Color(139, 209, 10)  // Verde brillante (para acentos)
    val buttonGreen = Color(198, 241, 119) // Verde claro (para botones)
    val backgroundColor = Color.White      // Fondo blanco
    val textColor = Color(60, 60, 60)      // Texto oscuro para mejor legibilidad
    val cardBackground = Color(250, 250, 250) // Gris muy claro para tarjetas

    val context = LocalContext.current
    var content by remember { mutableStateOf("") }
    var selectedEmotionId by remember { mutableStateOf<Int?>(null) }
    val postSuccess by noteViewModel.postSuccess.observeAsState()
    val emotions by emotionViewModel.emotions.observeAsState(emptyList())
    val imageUris = remember { mutableStateListOf<Uri>() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris.clear()
        imageUris.addAll(uris)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permiso denegado para acceder a la galería", Toast.LENGTH_SHORT).show()
        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                imageUris.clear()
                imageUris.addAll(uris)
            }
        }
    )

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
                    text = "New Note",
                    color = textColor,
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
                    containerColor = cardBackground
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                // Botón para seleccionar imágenes
                Button(onClick = {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    }

                    permissionLauncher.launch(permission)
                }) {
                    Text("Seleccionar imágenes")
                }
                // Vista previa de imágenes seleccionadas
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(imageUris.size) { index ->
                        Image(
                            painter = rememberAsyncImagePainter(imageUris[index]),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
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
                            color = textColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de texto para la nota
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Write your thoughts here...", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
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

                    Spacer(modifier = Modifier.height(20.dp))

                    // Dropdown para seleccionar emoción
                    Text(
                        text = "How are you feeling?",
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    EmotionDropdown(
                        emotions = emotions,
                        selectedEmotionId = selectedEmotionId,
                        onSelectEmotion = { selectedEmotionId = it },
                        accentColor = accentGreen,
                        textColor = textColor
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón para guardar
                    Button(
                        onClick = {
                            if (content.isNotBlank() && selectedEmotionId != null) {
                                val request = NewNoteRequest(content, selectedEmotionId!!)
                                Log.d("NewNoteScreen", "Creando nota: $request")
                                noteViewModel.createNote(context, request, imageUris)
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
    accentColor: Color,
    textColor: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedEmotion = emotions.find { it.id == selectedEmotionId }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = textColor
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                width = 1.dp
            )
        ) {
            Text(
                text = selectedEmotion?.name ?: "Select an emotion",
                color = if (selectedEmotion != null) accentColor else Color.Gray
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White)
        ) {
            emotions.forEach { emotion ->
                val emotionColor = try {
                    Color(android.graphics.Color.parseColor(emotion.color))
                } catch (e: Exception) {
                    accentColor
                }

                DropdownMenuItem(
                    text = {
                        Text(
                            emotion.name,
                            color = textColor,
                            fontWeight = if (emotion.id == selectedEmotionId)
                                FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(emotionColor, shape = RoundedCornerShape(4.dp))
                        )
                    },
                    onClick = {
                        onSelectEmotion(emotion.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
