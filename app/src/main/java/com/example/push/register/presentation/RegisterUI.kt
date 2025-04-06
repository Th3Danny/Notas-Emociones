package com.example.push.register.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.lifecycle.viewModelScope
import com.example.push.register.data.model.RegisterRequest
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    val name by viewModel.name.observeAsState("")
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val success by viewModel.success.observeAsState(false)

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
            placeholder = { Text(text = "Your name", fontWeight = FontWeight.SemiBold, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onChangeEmail(it) },
            placeholder = { Text(text = "Enter your email", fontWeight = FontWeight.SemiBold, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onChangePassword(it) },
            placeholder = { Text(text = "Enter your password", fontWeight = FontWeight.SemiBold, color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onChangePassword(it) },
            placeholder = { Text(text = "Confirm your password", fontWeight = FontWeight.SemiBold, color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.onSubmit(RegisterRequest(name, email, password))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(2.dp, color = Color(45, 105, 24))
        ) {
            Text(
                "Sign up",
                color = Color(45, 105, 24),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.onSubmit(RegisterRequest(name, email, password))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(198, 241, 119))
        ) {
            Text(
                "Sign in",
                color = Color(45, 105, 24),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}
