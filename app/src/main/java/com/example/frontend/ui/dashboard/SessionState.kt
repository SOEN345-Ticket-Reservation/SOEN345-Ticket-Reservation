package com.example.frontend.ui.dashboard

import com.example.frontend.model.UserResponse

data class SessionUiState(
    val isLoggedIn: Boolean,
    val userName: String,
    val userRole: String
)

fun onAuthSuccess(user: UserResponse): SessionUiState {
    return SessionUiState(
        isLoggedIn = true,
        userName = user.name,
        userRole = user.role
    )
}

fun onSignOut(): SessionUiState {
    return SessionUiState(
        isLoggedIn = false,
        userName = "",
        userRole = ""
    )
}
