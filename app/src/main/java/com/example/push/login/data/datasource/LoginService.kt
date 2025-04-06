package com.example.push.login.data.datasource

import LoginResponse
import com.example.push.login.data.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService{
    @POST("auth/authenticate")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}