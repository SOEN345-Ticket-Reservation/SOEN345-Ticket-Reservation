package com.example.frontend.model

import java.math.BigDecimal

enum class EventCategory {
    MOVIE, CONCERT, TRAVEL, SPORTS
}

data class EventResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val date: String,
    val location: String,
    val category: EventCategory,
    val capacity: Int,
    val price: BigDecimal,
    val availableSeats: Int
)

data class CreateEventRequest(
    val title: String,
    val description: String?,
    val date: String,
    val location: String,
    val category: EventCategory,
    val capacity: Int,
    val price: BigDecimal
)
