package com.example.frontend.ui.home

import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.repository.EventRepository
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

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

    @Test
    fun loadEvents_filtersAndSortsBySearchCategoryAndDate() = runTest {
        val repository = FakeEventRepository(
            listOf(
                event(id = 1L, title = "Alpha Night", date = "2026-04-12T10:00:00", category = EventCategory.MOVIE),
                event(id = 2L, title = "Beta Night", date = "2026-04-10T10:00:00", category = EventCategory.CONCERT),
                event(id = 3L, title = "Alpha Expo", date = "2026-04-11T10:00:00", category = EventCategory.MOVIE)
            )
        )
        val viewModel = HomeViewModel(repository)

        viewModel.loadEvents()
        advanceUntilIdle()

        assertEquals(listOf(1L, 3L, 2L), viewModel.events.value.map { it.id })

        viewModel.searchQuery.value = "Alpha"
        advanceUntilIdle()
        assertEquals(listOf(1L, 3L), viewModel.events.value.map { it.id })

        viewModel.selectedCategory.value = EventCategory.MOVIE
        advanceUntilIdle()
        assertEquals(listOf(1L, 3L), viewModel.events.value.map { it.id })

        viewModel.dateSortOrder.value = DateSortOrder.ASCENDING
        advanceUntilIdle()
        assertEquals(listOf(3L, 1L), viewModel.events.value.map { it.id })
    }

    private fun event(id: Long, date: String): EventResponse {
        return event(id, "Event $id", date, EventCategory.CONCERT)
    }

    private fun event(
        id: Long,
        title: String,
        date: String,
        category: EventCategory
    ): EventResponse {
        return EventResponse(
            id = id,
            title = title,
            description = null,
            date = date,
            location = "Main Hall",
            category = category,
            capacity = 100,
            price = BigDecimal.TEN,
            availableSeats = 50
        )
    }

    private class FakeEventRepository(
        private val events: List<EventResponse>
    ) : EventRepository() {
        override suspend fun getAllEvents(): Result<List<EventResponse>> = Result.success(events)
    }
}