package com.example.frontend.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.CreateEventRequest
import com.example.frontend.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminViewModel(
    private val adminRepository: AdminRepository = AdminRepository()
) : ViewModel() {

    fun createEvent(request: CreateEventRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            adminRepository.createEvent(request)
                .onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Failed to create event") }
        }
    }

    fun updateEvent(id: Long, request: CreateEventRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            adminRepository.updateEvent(id, request)
                .onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Failed to update event") }
        }
    }

    fun deleteEvent(id: Long, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            adminRepository.deleteEvent(id)
                .onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Failed to delete event") }
        }
    }
}
