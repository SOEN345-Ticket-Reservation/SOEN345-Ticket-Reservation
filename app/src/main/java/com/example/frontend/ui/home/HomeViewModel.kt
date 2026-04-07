package com.example.frontend.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.repository.EventRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventRepository: EventRepository = EventRepository()
) : ViewModel() {

    private val _events = MutableStateFlow<List<EventResponse>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<EventCategory?>(null)
    val dateSortOrder = MutableStateFlow(DateSortOrder.DESCENDING)

    val events: StateFlow<List<EventResponse>> = combine(
        _events, searchQuery, selectedCategory, dateSortOrder
    ) { events, query, category, sortOrder ->
        sortEventsByDate(
            events.filter { event ->
                (query.isBlank() || event.title.contains(query, ignoreCase = true) ||
                        event.location.contains(query, ignoreCase = true)) &&
                        (category == null || event.category == category)
            },
            sortOrder
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

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

enum class DateSortOrder {
    ASCENDING,
    DESCENDING
}

internal fun sortEventsByDate(
    events: List<EventResponse>,
    sortOrder: DateSortOrder
): List<EventResponse> {
    val sortedEvents = events.sortedBy { LocalDateTime.parse(it.date) }
    return when (sortOrder) {
        DateSortOrder.ASCENDING -> sortedEvents
        DateSortOrder.DESCENDING -> sortedEvents.reversed()
    }
}
