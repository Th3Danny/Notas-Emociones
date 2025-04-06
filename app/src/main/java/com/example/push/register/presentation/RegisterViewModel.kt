package com.example.push.register.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.push.register.data.model.RegisterRequest
import com.example.push.register.domain.RegisterUseCase
import com.google.firebase.messaging.FirebaseMessaging

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val navigateLogin: () -> Unit
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    private val _username = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _fcm = MutableLiveData<String>()
    private val _success = MutableLiveData<Boolean>()

    val name: LiveData<String> = _name
    val username: LiveData<String> = _username
    val email: LiveData<String> = _email
    val password: LiveData<String> = _password
    val fcm: LiveData<String> = _fcm
    val success: LiveData<Boolean> = _success

    init {
        //  Obtener token FCM al iniciar el ViewModel
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("RegisterViewModel", "Token FCM obtenido: $token")
                _fcm.value = token
            } else {
                Log.e("RegisterViewModel", "Error al obtener FCM: ${task.exception?.message}")
            }
        }
    }

    fun onChangeEmail(email: String) {
        _email.value = email
    }

    fun onChangeName(name: String) {
        _name.value = name
    }

    fun onChangeUserName(username: String) {
        _username.value = username
    }

    fun onChangePassword(password: String) {
        _password.value = password
    }

    suspend fun onSubmit() {
        val request = RegisterRequest(
            name = _name.value ?: "",
            username = _username.value ?: "",
            email = _email.value ?: "",
            password = _password.value ?: "",
            fcm = _fcm.value ?: ""
        )

        val result = registerUseCase(request)

        result.onSuccess {
            _success.postValue(true)

            // Navegar en el hilo principal
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                navigateLogin()
            }
        }

        result.onFailure { e ->
            Log.e("RegisterViewModel", "Registro fallido: ${e.message}", e)
            _success.postValue(false)
        }
    }


}
