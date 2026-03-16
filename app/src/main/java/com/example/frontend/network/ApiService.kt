package com.example.frontend.network

import com.example.frontend.model.CreateEventRequest
import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.model.LoginRequest
import com.example.frontend.model.RegisterRequest
import com.example.frontend.model.ReservationRequest
import com.example.frontend.model.ReservationResponse
import com.example.frontend.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // --- Auth ---

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): UserResponse

    // --- Events ---

    @GET("api/events")
    suspend fun getAllEvents(): List<EventResponse>

    @GET("api/events/{id}")
    suspend fun getEventById(@Path("id") id: Long): EventResponse

    @GET("api/events/category/{category}")
    suspend fun getEventsByCategory(@Path("category") category: EventCategory): List<EventResponse>

    @GET("api/events/location/{location}")
    suspend fun getEventsByLocation(@Path("location") location: String): List<EventResponse>

    @GET("api/events/date-range")
    suspend fun getEventsByDateRange(
        @Query("start") start: String,
        @Query("end") end: String
    ): List<EventResponse>

    // --- Reservations ---

    @POST("api/reservations")
    suspend fun createReservation(
        @Query("userId") userId: Long,
        @Body request: ReservationRequest
    ): ReservationResponse

    @PATCH("api/reservations/{id}/cancel")
    suspend fun cancelReservation(@Path("id") id: Long): ReservationResponse

    @GET("api/reservations/user/{userId}")
    suspend fun getUserReservations(@Path("userId") userId: Long): List<ReservationResponse>

    @GET("api/reservations/{id}")
    suspend fun getReservationById(@Path("id") id: Long): ReservationResponse

    // --- Admin ---

    @POST("api/admin/events")
    suspend fun createEvent(@Body request: CreateEventRequest): EventResponse

    @PUT("api/admin/events/{id}")
    suspend fun updateEvent(
        @Path("id") id: Long,
        @Body request: CreateEventRequest
    ): EventResponse

    @DELETE("api/admin/events/{id}")
    suspend fun deleteEvent(@Path("id") id: Long)
}
