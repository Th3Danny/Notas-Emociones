package com.example.push.register.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.push.register.data.model.RegisterRequest
import com.example.push.register.domain.RegisterUseCase

class RegisterViewModel(private val registerUseCase: RegisterUseCase, private val navigateLogin: () -> Unit ): ViewModel() {

    private val _name = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _success = MutableLiveData<Boolean>()

    val name: LiveData<String> = _name
    val email: LiveData<String> = _email
    val password: LiveData<String> = _password
    val success: LiveData<Boolean> = _success

    fun onChangeEmail (email: String) {
        _email.value = email
    }

    fun onChangeName (name: String) {
        _name.value = name
    }

    fun onChangePassword (password: String) {
        _password.value = password
    }

    suspend fun onSubmit(registerRequest: RegisterRequest) {
        val result = registerUseCase(registerRequest)

        result.onSuccess {
            navigateLogin()
        }
        result.onFailure { e ->
            Log.e("RegisterViewModel", "Register fallido: ${e.message}", e)
            _success.value = false
        }


    }
}