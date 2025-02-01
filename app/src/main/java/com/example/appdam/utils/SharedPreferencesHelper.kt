package com.example.appdam.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    companion object {

        private const val TOKEN_KEY = "token"
        private const val USERNAME_KEY = "userName"
        private const val EMAIL_KEY = "email"
    }

    // Salvar o token
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    // Recuperar o token
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Limpar o token
    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }

    // Salvar o nome do usuário
    fun saveUserName(userName: String) {
        sharedPreferences.edit().putString(USERNAME_KEY, userName).apply()
    }

    // Recuperar o nome do usuário
    fun getUserName(): String? {
        return sharedPreferences.getString(USERNAME_KEY, null)
    }

    // Salvar o email do usuário
    fun saveUserEmail(email: String) {
        sharedPreferences.edit().putString(EMAIL_KEY, email).apply()
    }

    // Recuperar o email do usuário
    fun getUserEmail(): String? {
        return sharedPreferences.getString(EMAIL_KEY, null)
    }
}
