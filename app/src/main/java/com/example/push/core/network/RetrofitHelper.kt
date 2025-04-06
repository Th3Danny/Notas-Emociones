package com.example.push.core.network

import com.example.push.core.session.SessionManager
import com.example.push.emotion.data.datasource.EmotionService
import com.example.push.login.data.datasource.LoginService

import com.example.push.notes.data.datasource.NotesService
import com.example.push.register.data.datasource.RegisterService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = sessionManager.getAuthToken()

        return if (token != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}

object RetrofitHelper {
    private const val BASE_URL = "http://44.205.80.158:8080/api/"
    private lateinit var sessionManager: SessionManager

    fun initialize(sessionManager: SessionManager) {
        this.sessionManager = sessionManager
    }

    private val okHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

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