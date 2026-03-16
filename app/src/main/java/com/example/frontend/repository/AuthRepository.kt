package com.example.frontend.repository

import com.example.frontend.model.LoginRequest
import com.example.frontend.model.RegisterRequest
import com.example.frontend.model.UserResponse
import com.example.frontend.network.ApiClient

class AuthRepository {

    private val api = ApiClient.apiService

    suspend fun register(request: RegisterRequest): Result<UserResponse> = runCatching {
        api.register(request)
    }

    suspend fun login(emailOrPhone: String, password: String): Result<UserResponse> = runCatching {
        api.login(LoginRequest(emailOrPhone, password))
    }
}
