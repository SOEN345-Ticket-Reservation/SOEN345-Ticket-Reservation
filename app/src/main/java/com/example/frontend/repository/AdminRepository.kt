package com.example.frontend.repository

import com.example.frontend.model.CreateEventRequest
import com.example.frontend.model.EventResponse
import com.example.frontend.network.ApiClient

class AdminRepository {

    private val api = ApiClient.apiService

    suspend fun createEvent(request: CreateEventRequest): Result<EventResponse> = runCatching {
        api.createEvent(request)
    }

    suspend fun updateEvent(id: Long, request: CreateEventRequest): Result<EventResponse> = runCatching {
        api.updateEvent(id, request)
    }

    suspend fun deleteEvent(id: Long): Result<Unit> = runCatching {
        api.deleteEvent(id)
    }
}
