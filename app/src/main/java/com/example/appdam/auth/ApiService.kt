package com.example.appdam.auth

import com.example.appdam.models.LoginRequest
import com.example.appdam.models.LoginResponse
import com.example.appdam.models.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<Void>
}
