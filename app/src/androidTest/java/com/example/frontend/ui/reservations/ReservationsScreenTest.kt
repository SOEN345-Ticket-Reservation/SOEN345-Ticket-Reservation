package com.example.frontend.ui.reservations

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.example.frontend.auth.ReservationNotificationPrefs
import com.example.frontend.auth.TokenStorage
import com.example.frontend.model.ReservationResponse
import com.example.frontend.model.ReservationStatus
import com.example.frontend.repository.FakeReservationRepository
import com.example.frontend.ui.theme.FrontendTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for ReservationsScreen: empty state, and list with Cancel/Notify.
 */
@Suppress("TestFunctionName")
class ReservationsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        TokenStorage.init(context)
        TokenStorage.setUserId(1L)
        ReservationNotificationPrefs.init(context)
    }

    @Test
    fun ReservationsScreen_whenEmpty_displaysNoReservations() {
        val viewModel = ReservationsViewModel(FakeReservationRepository(userReservations = emptyList()))
        composeTestRule.setContent {
            FrontendTheme {
                ReservationsScreen(showSnackbar = {}, viewModel = viewModel)
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("No reservations").assertIsDisplayed()
    }

    @Test
    fun ReservationsScreen_withReservation_displaysEventTitleCancelAndNotify() {
        val fakeList = listOf(
            ReservationResponse(
                id = 1L,
                userId = 1L,
                userName = "User",
                eventId = 10L,
                eventTitle = "Test Concert",
                status = ReservationStatus.CONFIRMED,
                reservedAt = "2026-01-15T19:00:00",
                confirmationCode = "ABC123",
                numberOfTickets = 2
            )
        )
        val viewModel = ReservationsViewModel(FakeReservationRepository(userReservations = fakeList))
        composeTestRule.setContent {
            FrontendTheme {
                ReservationsScreen(showSnackbar = {}, viewModel = viewModel)
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Test Concert").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Notify").assertIsDisplayed()
    }
}
