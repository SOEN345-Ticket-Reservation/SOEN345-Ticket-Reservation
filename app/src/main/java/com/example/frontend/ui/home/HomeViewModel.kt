package com.example.frontend.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.EventResponse
import com.example.frontend.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventRepository: EventRepository = EventRepository()
) : ViewModel() {

    private val _events = MutableStateFlow<List<EventResponse>>(emptyList())
    val events: StateFlow<List<EventResponse>> = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            eventRepository.getAllEvents()
                .onSuccess { _events.value = it }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }
}
