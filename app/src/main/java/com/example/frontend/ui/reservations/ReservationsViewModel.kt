package com.example.frontend.ui.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.auth.ReservationNotificationPrefs
import com.example.frontend.auth.TokenStorage
import com.example.frontend.model.ReservationResponse
import com.example.frontend.repository.ReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReservationsViewModel(
    private val reservationRepository: ReservationRepository = ReservationRepository()
) : ViewModel() {

    private val _reservations = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val reservations: StateFlow<List<ReservationResponse>> = _reservations.asStateFlow()

    private val _notifyEnabledIds = MutableStateFlow<Set<Long>>(emptySet())
    val notifyEnabledIds: StateFlow<Set<Long>> = _notifyEnabledIds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadReservations(onError: (String) -> Unit) {
        val userId = TokenStorage.getUserId() ?: run {
            onError("Not logged in")
            return
        }
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            reservationRepository.getUserReservations(userId)
                .onSuccess { list ->
                    _reservations.value = list
                    _notifyEnabledIds.value = list.map { it.id }.filter { id ->
                        ReservationNotificationPrefs.getNotify(id)
                    }.toSet()
                }
                .onFailure {
                    _errorMessage.value = it.message
                    onError(it.message ?: "Failed to load reservations")
                }
            _isLoading.value = false
        }
    }

    fun cancelReservation(
        reservationId: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            reservationRepository.cancelReservation(reservationId)
                .onSuccess { response ->
                    _reservations.value = _reservations.value.map {
                        if (it.id == response.id) response else it
                    }
                    onSuccess()
                }
                .onFailure {
                    onError(it.message ?: "Failed to cancel")
                }
        }
    }

    fun setNotify(reservationId: Long, enabled: Boolean) {
        ReservationNotificationPrefs.setNotify(reservationId, enabled)
        _notifyEnabledIds.value = if (enabled) {
            _notifyEnabledIds.value + reservationId
        } else {
            _notifyEnabledIds.value - reservationId
        }
    }
}
