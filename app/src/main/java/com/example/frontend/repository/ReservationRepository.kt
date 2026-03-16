package com.example.frontend.repository

import com.example.frontend.model.ReservationRequest
import com.example.frontend.model.ReservationResponse
import com.example.frontend.network.ApiClient

class ReservationRepository {

    private val api = ApiClient.apiService

    suspend fun createReservation(
        userId: Long,
        eventId: Long,
        numberOfTickets: Int = 1
    ): Result<ReservationResponse> = runCatching {
        api.createReservation(userId, ReservationRequest(eventId, numberOfTickets))
    }

    suspend fun cancelReservation(reservationId: Long): Result<ReservationResponse> = runCatching {
        api.cancelReservation(reservationId)
    }

    suspend fun getUserReservations(userId: Long): Result<List<ReservationResponse>> = runCatching {
        api.getUserReservations(userId)
    }

    suspend fun getReservationById(id: Long): Result<ReservationResponse> = runCatching {
        api.getReservationById(id)
    }
}
