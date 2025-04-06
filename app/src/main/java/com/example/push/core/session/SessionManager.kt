package com.example.push.core.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun getAuthToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt("userId", -1)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}