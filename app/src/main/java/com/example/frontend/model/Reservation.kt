package com.example.frontend.model

enum class ReservationStatus {
    CONFIRMED, CANCELLED
}

data class ReservationRequest(
    val eventId: Long,
    val numberOfTickets: Int = 1
)

data class ReservationResponse(
    val id: Long,
    val userId: Long,
    val userName: String,
    val eventId: Long,
    val eventTitle: String,
    val status: ReservationStatus,
    val reservedAt: String,
    val confirmationCode: String,
    val numberOfTickets: Int
)
