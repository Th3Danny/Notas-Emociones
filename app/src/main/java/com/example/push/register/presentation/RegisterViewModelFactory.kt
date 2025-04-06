package com.example.push.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.push.register.domain.RegisterUseCase

class RegisterViewModelFactory(
    private val registerUseCase: RegisterUseCase,
    private val navigateLogin: () -> Unit
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(registerUseCase, navigateLogin) as T
    }
}