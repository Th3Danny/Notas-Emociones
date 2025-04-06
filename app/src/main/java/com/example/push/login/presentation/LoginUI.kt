package com.example.push.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val isSuccess by viewModel.isSuccess.observeAsState()

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to", color = Color(45, 105, 24), fontSize = 40.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("Lumimood", color = Color(139, 209, 10), fontSize = 40.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(100.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onChangeEmail(it) },
            placeholder = { Text(text = "Enter your email", fontWeight = FontWeight.SemiBold, color = Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onChangePassword(it) },
            placeholder = { Text(text = "Enter your password", fontWeight = FontWeight.SemiBold, color = Color.Black) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { viewModel.onSubmit() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(198, 241, 119))
        ) {
            Text("Login", color = Color(45, 105, 24), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Text(text = "Don't have an account?", color = Color.Gray, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Sign up",
                color = Color(139, 209, 10),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { viewModel.navigateRegister() }
            )

        }
    }
}






