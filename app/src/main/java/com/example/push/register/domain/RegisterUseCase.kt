package com.example.push.register.domain

import com.example.push.register.data.model.RegisterRequest
import com.example.push.register.data.model.RegisterResponse
import com.example.push.register.data.repository.RegisterRepository


class RegisterUseCase(private val registerRepository: RegisterRepository) {

    suspend operator fun invoke(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return registerRepository.register(registerRequest)
    }
}