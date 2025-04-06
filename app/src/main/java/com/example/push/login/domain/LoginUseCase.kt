package com.example.push.login.domain

import LoginResponse
import com.example.push.login.data.model.LoginRequest
import com.example.push.login.data.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            repository.login(loginRequest)
        } catch (e: Exception) {
            // Manejo de excepciones
            Result.failure(e)
        }
    }
}
