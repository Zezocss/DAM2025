package com.example.appdam.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "token"
        private const val PRIVACY_POLICY_KEY = "TermosDePrivacidadeAceites"
    }

    // Salvar o token de autenticação
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    // Recuperar o token de autenticação
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Limpar o token de autenticação
    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }

    // Guardar a aceitação da política de privacidade
    fun savePrivacyPolicyAccepted() {
        sharedPreferences.edit().putBoolean(PRIVACY_POLICY_KEY, true).apply()
    }

    // Verificar se a política de privacidade foi aceite
    fun isPrivacyPolicyAccepted(): Boolean {
        return sharedPreferences.getBoolean(PRIVACY_POLICY_KEY, false)
    }

    // Reset aceitação da política de privacidade
    fun resetPrivacyPolicy() {
        sharedPreferences.edit().putBoolean(PRIVACY_POLICY_KEY, false).apply()
    }
}

