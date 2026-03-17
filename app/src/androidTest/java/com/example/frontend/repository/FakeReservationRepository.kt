package com.example.frontend.repository

import com.example.frontend.model.ReservationResponse
import com.example.frontend.model.ReservationStatus

/**
 * Fake repository for tests. Returns configurable results without network.
 */
class FakeReservationRepository(
    private val userReservations: List<ReservationResponse> = emptyList(),
    private val cancelResponse: ReservationResponse? = null
) : ReservationRepository() {

    override suspend fun getUserReservations(userId: Long): Result<List<ReservationResponse>> =
        Result.success(userReservations)

    override suspend fun cancelReservation(reservationId: Long): Result<ReservationResponse> =
        cancelResponse?.let { Result.success(it) }
            ?: Result.success(
                userReservations.find { it.id == reservationId }?.copy(status = ReservationStatus.CANCELLED)
                    ?: ReservationResponse(
                        id = reservationId,
                        userId = 1L,
                        userName = "User",
                        eventId = 1L,
                        eventTitle = "Event",
                        status = ReservationStatus.CANCELLED,
                        reservedAt = "",
                        confirmationCode = "XXX",
                        numberOfTickets = 1
                    )
            )
}
