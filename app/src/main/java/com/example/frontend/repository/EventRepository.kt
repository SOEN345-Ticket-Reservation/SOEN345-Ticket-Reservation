package com.example.frontend.repository

import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.network.ApiClient

class EventRepository {

    private val api = ApiClient.apiService

    suspend fun getAllEvents(): Result<List<EventResponse>> = runCatching {
        api.getAllEvents()
    }

    suspend fun getEventById(id: Long): Result<EventResponse> = runCatching {
        api.getEventById(id)
    }

    suspend fun getEventsByCategory(category: EventCategory): Result<List<EventResponse>> = runCatching {
        api.getEventsByCategory(category)
    }

    suspend fun getEventsByLocation(location: String): Result<List<EventResponse>> = runCatching {
        api.getEventsByLocation(location)
    }

    suspend fun getEventsByDateRange(start: String, end: String): Result<List<EventResponse>> = runCatching {
        api.getEventsByDateRange(start, end)
    }
}
