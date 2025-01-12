package com.example.appdam

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.model.UserState
import data.network.SupabaseClient
import data.network.SupabaseClient.client
import io.github.jan.supabase.gotrue.admin.AdminUserBuilder
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SupabaseAuthViewModel: ViewModel() {
    private val _userState = mutableStateOf<UserState>(UserState.Loading)
    val userState: State<UserState> = _userState

    fun signUp(
        context: Context,
        userEmail: String,
        userPassword: String,
    ){
        viewModelScope.launch { this: CoroutineScope
            try{
                client.gotrue.signUpWith(Email){ this: Email.Config

                }
            } catch (e: Exception){

            }
        }
    }
}