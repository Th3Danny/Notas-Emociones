package com.example.push.notes.presentation

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.push.emotion.data.model.EmotionResponse
import com.example.push.emotion.presentation.EmotionViewModel
import com.example.push.notes.data.model.NewNoteRequest
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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

    // Para guardar la ruta del archivo de la cámara
    var currentPhotoPath by remember { mutableStateOf<String?>(null) }
    var photoFile by remember { mutableStateOf<File?>(null) }

    // Función para crear un archivo temporal para la foto
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
            Log.d("CameraDebug", "Archivo creado en: $absolutePath")
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris.clear()
        imageUris.addAll(uris)
        Log.d("ImagePicker", "Imágenes seleccionadas: ${uris.size}")
    }

    // URI para la imagen de la cámara
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let { uri ->
                imageUris.add(uri)
                Log.d("CameraCapture", "Foto capturada con éxito: $uri")
                Log.d("CameraCapture", "Path del archivo: $currentPhotoPath")
                Log.d("CameraCapture", "Número total de imágenes: ${imageUris.size}")

                // Verificar acceso a la imagen
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        Log.d("CameraCapture", "URI accesible: stream abierto correctamente")
                    }
                } catch (e: Exception) {
                    Log.e("CameraCapture", "Error al verificar acceso a URI", e)
                }

                Toast.makeText(context, "Foto capturada con éxito", Toast.LENGTH_SHORT).show()
            } ?: run {
                Log.e("CameraCapture", "URI es nula después de captura exitosa")
                Toast.makeText(context, "Error al procesar la foto", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("CameraCapture", "La captura falló o fue cancelada")
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                // Crear un archivo para la foto
                photoFile = createImageFile()
                photoFile?.let { file ->
                    // Usar FileProvider para crear un URI para el archivo
                    val photoURI = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    Log.d("CameraCapture", "URI creado con FileProvider: $photoURI")

                    // Guardar la URI y lanzar la cámara
                    cameraImageUri.value = photoURI
                    cameraLauncher.launch(photoURI)
                } ?: run {
                    Log.e("CameraCapture", "No se pudo crear el archivo para la foto")
                    Toast.makeText(context, "Error al preparar la cámara", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CameraCapture", "Error al preparar la cámara", e)
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permiso denegado para acceder a la galería", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para tomar foto
    val takePhoto = {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PermissionChecker.PERMISSION_GRANTED) {
            // Solicitar permiso si no está concedido
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            // Si ya tiene permiso, proceder directamente
            try {
                photoFile = createImageFile()
                photoFile?.let { file ->
                    val photoURI = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    Log.d("CameraCapture", "URI creado con FileProvider: $photoURI")
                    cameraImageUri.value = photoURI
                    cameraLauncher.launch(photoURI)
                }
            } catch (e: Exception) {
                Log.e("CameraCapture", "Error al preparar la cámara", e)
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                imageUris.clear()
                noteViewModel.clearPostSuccess()
                onNoteCreated()
            }
            false -> {
                Toast.makeText(context, "Nota guardada localmente (sin conexión)", Toast.LENGTH_LONG).show()
                content = ""
                selectedEmotionId = null
                imageUris.clear()
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Botones para imágenes
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { takePhoto() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Tomar foto")
                        }

                        Button(
                            onClick = {
                                val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    Manifest.permission.READ_MEDIA_IMAGES
                                } else {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                }
                                storagePermissionLauncher.launch(permission)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Galería")
                        }
                    }

                    // Vista previa de imágenes seleccionadas
                    if (imageUris.isNotEmpty()) {
                        Text(
                            text = "Imágenes seleccionadas (${imageUris.size})",
                            fontSize = 14.sp,
                            color = textColor,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(imageUris.size) { index ->
                                val uri = imageUris[index]
                                Log.d("ImagePreview", "Mostrando imagen $index: $uri")

                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.LightGray)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = uri,
                                            onState = { state ->
                                                when (state) {
                                                    is AsyncImagePainter.State.Success -> {
                                                        Log.d("ImagePreview", "Imagen $index cargada correctamente")
                                                    }
                                                    is AsyncImagePainter.State.Error -> {
                                                        Log.e("ImagePreview", "Error al cargar imagen $uri", state.result.throwable)
                                                    }
                                                    else -> {}
                                                }
                                            }
                                        ),
                                        contentDescription = "Selected Image",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    // Icono y título de la nota
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
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
                                Log.d("NewNoteScreen", "Creando nota: $request con ${imageUris.size} imágenes")
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