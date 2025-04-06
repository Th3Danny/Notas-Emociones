package com.example.push.register.presentation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.register.data.model.RegisterRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    val context = LocalContext.current

    val name by viewModel.name.observeAsState("")
    val username by viewModel.username.observeAsState("")
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val fcm by viewModel.fcm.observeAsState("")
    val success by viewModel.success.observeAsState()

    LaunchedEffect(success) {
        when (success) {
            true -> Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show()
            null -> Unit
        }
    }

    Column(
        modifier = Modifier
            .background(Color(235, 235, 235))
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Lumimood",
            color = Color(45, 105, 24),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.onChangeName(it) },
            placeholder = { Text("Your name", fontWeight = FontWeight.SemiBold, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { viewModel.onChangeUserName(it) },
            placeholder = { Text("Your username", fontWeight = FontWeight.SemiBold, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onChangeEmail(it) },
            placeholder = { Text("Enter your email", fontWeight = FontWeight.SemiBold, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onChangePassword(it) },
            placeholder = { Text("Enter your password", fontWeight = FontWeight.SemiBold, color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.onSubmit()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(198, 241, 119))
        ) {
            Text(
                "Sign up",
                color = Color(45, 105, 24),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}
