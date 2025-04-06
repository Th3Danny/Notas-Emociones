package com.example.push.login.data.repository

import LoginResponse
import com.example.push.core.network.RetrofitHelper
import com.example.push.login.data.model.LoginRequest


class LoginRepository {
    private val loginService = RetrofitHelper.loginService

    suspend fun login (request : LoginRequest): Result<LoginResponse> {
        return try {
            val response = loginService.login(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("El cuerpo de la respuesta es nulo"))
                }
            } else {
                val errorMsg = response.errorBody()?.string()
                Result.failure(Exception("Error HTTP: ${response.code()} - $errorMsg"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}