package com.example.frontend.ui.home

import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun sortEventsByDate_sortsAscendingAndDescending() {
        val events = listOf(
            event(id = 1L, date = "2026-04-12T10:00:00"),
            event(id = 2L, date = "2026-04-10T10:00:00"),
            event(id = 3L, date = "2026-04-11T10:00:00")
        )

        val ascending = sortEventsByDate(events, DateSortOrder.ASCENDING)
        val descending = sortEventsByDate(events, DateSortOrder.DESCENDING)

        assertEquals(listOf(2L, 3L, 1L), ascending.map { it.id })
        assertEquals(listOf(1L, 3L, 2L), descending.map { it.id })
    }

    private fun event(id: Long, date: String): EventResponse {
        return EventResponse(
            id = id,
            title = "Event $id",
            description = null,
            date = date,
            location = "Main Hall",
            category = EventCategory.CONCERT,
            capacity = 100,
            price = BigDecimal.TEN,
            availableSeats = 50
        )
    }
}