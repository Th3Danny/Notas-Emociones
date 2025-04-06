package com.example.push.core.network

import com.example.push.emotion.data.datasource.EmotionService
import com.example.push.login.data.datasource.LoginService

import com.example.push.notes.data.datasource.NotesService
import com.example.push.register.data.datasource.RegisterService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object RetrofitHelper {

    private const val BASE_URL = "http://44.205.80.158:8080/api/"

    internal val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    val registerService: RegisterService by lazy {
        retrofit.create(RegisterService::class.java)
    }


    val notesService: NotesService by lazy {
        retrofit.create(NotesService::class.java)
    }

    val emotionService: EmotionService by lazy {
        retrofit.create(EmotionService::class.java)
    }


}