package com.example.push.login.presentation

import android.content.Context
import android.provider.Settings.Global.putInt
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.push.login.data.model.LoginRequest
import com.example.push.login.domain.LoginUseCase
import kotlinx.coroutines.launch


class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val navigateToHome: () -> Unit,
    private val context: Context,
    private val navigateToRegister: () -> Unit
) : ViewModel() {


    private val _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private val _userId = MutableLiveData("")
    val userId: LiveData<String> get() = _userId

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess: LiveData<Boolean?> get() = _isSuccess

    fun onChangeEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun onChangePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun onSubmit() {
        viewModelScope.launch {
            val result = loginUseCase(LoginRequest(email.value ?: "", password.value ?: ""))
            result.onSuccess { response ->
                val sharedPrefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

                sharedPrefs.edit()
                    .putString("access_token", response.data.access_token)
                    .putInt("userId", response.data.id_user)
                    .putBoolean("isLoggedIn", true)
                    .apply()

                Log.d("LoginViewModel", "Login exitoso. Token y userId guardados.")
                _isSuccess.value = true
                navigateToHome()
            }
            result.onFailure { e ->
                Log.e("LoginViewModel", "Login fallido: ${e.message}", e)
                _isSuccess.value = false
            }
        }
    }



    fun navigateRegister() {
        navigateToRegister()
    }


    private fun saveProfileId(idProfile: Int) {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("idProfile", idProfile)
            apply()
        }
        Log.d("LoginViewModel", " idProfile guardado en SharedPreferences: $idProfile")
    }

    private fun saveSession(userId: Int) {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isLoggedIn", true)
            putInt("userId", userId)
            apply()
        }
    }


    internal fun logout(context: Context) {
        val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}
