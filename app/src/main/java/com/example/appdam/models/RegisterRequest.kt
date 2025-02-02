
package com.example.appdam.models
//modelo para o registo de um novo user
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val password: String
)

