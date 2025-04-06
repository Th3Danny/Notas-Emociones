package com.example.push.register.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val username:String,
    val name: String,
    val fcm : String
)
