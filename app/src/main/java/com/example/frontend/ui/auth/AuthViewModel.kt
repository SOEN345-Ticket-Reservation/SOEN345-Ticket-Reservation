package com.example.frontend.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.RegisterRequest
import com.example.frontend.model.UserResponse
import com.example.frontend.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val name = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val isAdmin = MutableStateFlow(false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun login(onSuccess: (UserResponse) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            authRepository.login(email.value.trim(), password.value)
                .onSuccess {
                    _isLoading.value = false
                    onSuccess(it)
                }
                .onFailure {
                    _isLoading.value = false
                    _errorMessage.value = it.message ?: "Login failed"
                }
        }
    }

    fun register(onSuccess: (UserResponse) -> Unit) {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            authRepository.register(
                RegisterRequest(
                    name = name.value.trim(),
                    email = email.value.trim(),
                    phone = phone.value.trim().takeIf { it.isNotEmpty() },
                    password = password.value,
                    role = if (isAdmin.value) "ADMIN" else "CUSTOMER"
                )
            )
                .onSuccess {
                    _isLoading.value = false
                    onSuccess(it)
                }
                .onFailure {
                    _isLoading.value = false
                    _errorMessage.value = it.message ?: "Registration failed"
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
