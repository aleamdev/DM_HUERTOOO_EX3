package com.dewis.huertohogar.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginBody(
    val email: String,
    val password: String
)

data class RegisterBody(
    val email: String,
    val password: String,
    val nombre: String
)

data class AuthResponseDto(
    val token: String,
    val tipo: String,
    val email: String,
    val rol: String
)

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginBody): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body body: RegisterBody): AuthResponseDto
}
