package com.example.push.login.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.push.login.domain.LoginUseCase

class LoginViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val navigateToHome: () -> Unit,
    private val context: Context,
    private val navigateToRegister: () -> Unit
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginUseCase, navigateToHome, context, navigateToRegister) as T
    }
}
