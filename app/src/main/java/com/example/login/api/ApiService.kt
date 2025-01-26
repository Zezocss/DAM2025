package com.example.login.api

import com.example.login.models.LoginRequest
import com.example.login.models.LoginResponse
import com.example.login.models.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<Void>
}
