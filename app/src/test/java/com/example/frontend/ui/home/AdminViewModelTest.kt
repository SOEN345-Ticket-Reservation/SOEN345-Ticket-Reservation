package com.example.frontend.ui.home

import com.example.frontend.model.CreateEventRequest
import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.repository.AdminRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import java.math.BigDecimal
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun createEvent_successCallsRepositoryAndSuccessCallback() = runTest {
        val repository = FakeAdminRepository(
            createResult = Result.success(sampleEventResponse())
        )
        val viewModel = AdminViewModel(repository)
        val request = sampleRequest()
        var successCalled = false
        var errorMessage: String? = null

        viewModel.createEvent(request, onSuccess = { successCalled = true }, onError = { errorMessage = it })
        advanceUntilIdle()

        assertTrue(successCalled)
        assertNull(errorMessage)
        assertEquals(request, repository.lastCreateRequest)
    }

    @Test
    fun updateEvent_failureCallsErrorCallback() = runTest {
        val repository = FakeAdminRepository(
            updateResult = Result.failure(IllegalStateException("Update failed"))
        )
        val viewModel = AdminViewModel(repository)
        val request = sampleRequest()
        var successCalled = false
        var errorMessage: String? = null

        viewModel.updateEvent(7L, request, onSuccess = { successCalled = true }, onError = { errorMessage = it })
        advanceUntilIdle()

        assertFalse(successCalled)
        assertEquals("Update failed", errorMessage)
        assertEquals(7L, repository.lastUpdateId)
        assertEquals(request, repository.lastUpdateRequest)
    }

    @Test
    fun deleteEvent_successCallsSuccessCallback() = runTest {
        val repository = FakeAdminRepository(deleteResult = Result.success(Unit))
        val viewModel = AdminViewModel(repository)
        var successCalled = false
        var errorMessage: String? = null

        viewModel.deleteEvent(5L, onSuccess = { successCalled = true }, onError = { errorMessage = it })
        advanceUntilIdle()

        assertTrue(successCalled)
        assertNull(errorMessage)
        assertEquals(5L, repository.lastDeleteId)
    }

    private fun sampleRequest(): CreateEventRequest {
        return CreateEventRequest(
            title = "Concert Night",
            description = "Live music",
            date = "2026-04-15T20:00:00",
            location = "Main Hall",
            category = EventCategory.CONCERT,
            capacity = 120,
            price = BigDecimal("49.99")
        )
    }

    private fun sampleEventResponse(): EventResponse {
        return defaultEventResponse()
    }

    private class FakeAdminRepository(
        private val createResult: Result<EventResponse> = Result.success(defaultEventResponse()),
        private val updateResult: Result<EventResponse> = Result.success(defaultEventResponse()),
        private val deleteResult: Result<Unit> = Result.success(Unit)
    ) : AdminRepository() {

        var lastCreateRequest: CreateEventRequest? = null
        var lastUpdateId: Long? = null
        var lastUpdateRequest: CreateEventRequest? = null
        var lastDeleteId: Long? = null

        override suspend fun createEvent(request: CreateEventRequest): Result<EventResponse> {
            lastCreateRequest = request
            return createResult
        }

        override suspend fun updateEvent(id: Long, request: CreateEventRequest): Result<EventResponse> {
            lastUpdateId = id
            lastUpdateRequest = request
            return updateResult
        }

        override suspend fun deleteEvent(id: Long): Result<Unit> {
            lastDeleteId = id
            return deleteResult
        }
    }
}

private fun defaultEventResponse(): EventResponse {
    return EventResponse(
        id = 1L,
        title = "Concert Night",
        description = "Live music",
        date = "2026-04-15T20:00:00",
        location = "Main Hall",
        category = EventCategory.CONCERT,
        capacity = 120,
        price = BigDecimal("49.99"),
        availableSeats = 60
    )
}