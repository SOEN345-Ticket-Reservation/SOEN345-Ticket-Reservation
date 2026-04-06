package com.example.frontend.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String?,
    val password: String,
    val role: String = "CUSTOMER"
)

data class LoginRequest(
    val emailOrPhone: String,
    val password: String
)

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String?,
    val role: String,
    val token: String? = null
)
