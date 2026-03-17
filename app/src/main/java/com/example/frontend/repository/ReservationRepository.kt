package com.example.frontend.repository

import com.example.frontend.model.ReservationRequest
import com.example.frontend.model.ReservationResponse
import com.example.frontend.network.ApiClient

open class ReservationRepository {

    private val api = ApiClient.apiService

    open suspend fun createReservation(
        userId: Long,
        eventId: Long,
        numberOfTickets: Int = 1
    ): Result<ReservationResponse> = runCatching {
        api.createReservation(userId, ReservationRequest(eventId, numberOfTickets))
    }

    open suspend fun cancelReservation(reservationId: Long): Result<ReservationResponse> = runCatching {
        api.cancelReservation(reservationId)
    }

    open suspend fun getUserReservations(userId: Long): Result<List<ReservationResponse>> = runCatching {
        api.getUserReservations(userId)
    }

    open suspend fun getReservationById(id: Long): Result<ReservationResponse> = runCatching {
        api.getReservationById(id)
    }
}
