package com.example.frontend.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.example.frontend.auth.TokenStorage
import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.model.ReservationResponse
import com.example.frontend.model.ReservationStatus
import com.example.frontend.repository.EventRepository
import com.example.frontend.repository.ReservationRepository
import com.example.frontend.ui.theme.FrontendTheme
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("TestFunctionName")
class EventDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        TokenStorage.init(context)
        TokenStorage.setUserId(99L)
    }

    @Test
    fun EventDetailScreen_displaysEventDetails() {
        val event = sampleEvent()

        composeTestRule.setContent {
            FrontendTheme {
                EventDetailScreen(
                    eventId = event.id,
                    onBack = { },
                    showSnackbar = { },
                    eventRepository = FakeEventRepository(event),
                    reservationRepository = FakeReservationRepository()
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Jazz Night").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date: 2026-04-15T20:00:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("Location: Main Hall").assertIsDisplayed()
        composeTestRule.onNodeWithText("Price: $25.00").assertIsDisplayed()
        composeTestRule.onNodeWithText("Available seats: 15").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reserve").assertIsDisplayed()
    }

    @Test
    fun EventDetailScreen_reserveButtonCallsRepositoryAndSnackbar() {
        val event = sampleEvent()
        val fakeReservationRepository = FakeReservationRepository()
        var snackbarMessage: String? = null

        composeTestRule.setContent {
            FrontendTheme {
                EventDetailScreen(
                    eventId = event.id,
                    onBack = { },
                    showSnackbar = { snackbarMessage = it },
                    eventRepository = FakeEventRepository(event),
                    reservationRepository = fakeReservationRepository
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Reserve").performClick()
        composeTestRule.waitForIdle()

        assertEquals(99L, fakeReservationRepository.lastUserId)
        assertEquals(event.id, fakeReservationRepository.lastEventId)
        assertEquals(1, fakeReservationRepository.lastNumberOfTickets)
        assertEquals("Reserved!", snackbarMessage)
        assertTrue(fakeReservationRepository.called)
    }

    private fun sampleEvent(): EventResponse {
        return EventResponse(
            id = 8L,
            title = "Jazz Night",
            description = "Live jazz",
            date = "2026-04-15T20:00:00",
            location = "Main Hall",
            category = EventCategory.CONCERT,
            capacity = 100,
            price = BigDecimal("25.00"),
            availableSeats = 15
        )
    }

    private class FakeEventRepository(
        private val event: EventResponse
    ) : EventRepository() {
        override suspend fun getEventById(id: Long): Result<EventResponse> = Result.success(event)
    }

    private class FakeReservationRepository : ReservationRepository() {
        var called = false
        var lastUserId: Long? = null
        var lastEventId: Long? = null
        var lastNumberOfTickets: Int? = null

        override suspend fun createReservation(
            userId: Long,
            eventId: Long,
            numberOfTickets: Int
        ): Result<ReservationResponse> {
            called = true
            lastUserId = userId
            lastEventId = eventId
            lastNumberOfTickets = numberOfTickets
            return Result.success(
                ReservationResponse(
                    id = 1L,
                    userId = userId,
                    userName = "User",
                    eventId = eventId,
                    eventTitle = "Jazz Night",
                    status = ReservationStatus.CONFIRMED,
                    reservedAt = "2026-04-15T20:01:00",
                    confirmationCode = "ABC123",
                    numberOfTickets = numberOfTickets
                )
            )
        }
    }
}