package com.example.frontend.repository

import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.network.ApiClient

open class EventRepository {

    private val api = ApiClient.apiService

    open suspend fun getAllEvents(): Result<List<EventResponse>> = runCatching {
        api.getAllEvents()
    }

    open suspend fun getEventById(id: Long): Result<EventResponse> = runCatching {
        api.getEventById(id)
    }

    open suspend fun getEventsByCategory(category: EventCategory): Result<List<EventResponse>> = runCatching {
        api.getEventsByCategory(category)
    }

    open suspend fun getEventsByLocation(location: String): Result<List<EventResponse>> = runCatching {
        api.getEventsByLocation(location)
    }

    open suspend fun getEventsByDateRange(start: String, end: String): Result<List<EventResponse>> = runCatching {
        api.getEventsByDateRange(start, end)
    }
}
