package com.example.frontend.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.repository.AdminRepository
import com.example.frontend.repository.EventRepository
import com.example.frontend.ui.theme.FrontendTheme
import java.math.BigDecimal
import org.junit.Rule
import org.junit.Test

@Suppress("TestFunctionName")
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun HomeScreen_displaysFilters_andShowsLoadedEvents() {
        val homeViewModel = HomeViewModel(FakeEventRepository(sampleEvents()))
        homeViewModel.searchQuery.value = "Alpha"
        homeViewModel.selectedCategory.value = EventCategory.CONCERT
        homeViewModel.dateSortOrder.value = DateSortOrder.ASCENDING

        composeTestRule.setContent {
            FrontendTheme {
                HomeScreen(
                    userRole = "USER",
                    onEventClick = { },
                    showSnackbar = { },
                    viewModel = homeViewModel,
                    adminViewModel = AdminViewModel(FakeAdminRepository())
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Search events...").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ascending").assertIsDisplayed().assertIsSelected()
        composeTestRule.onNodeWithText("Descending").assertIsDisplayed()
        composeTestRule.onNodeWithText("All").assertIsDisplayed()
        composeTestRule.onNodeWithText("Alpha Concert").assertIsDisplayed()
    }

    @Test
    fun HomeScreen_clickingDateSortChip_updatesSelection() {
        val homeViewModel = HomeViewModel(FakeEventRepository(sampleEvents()))

        composeTestRule.setContent {
            FrontendTheme {
                HomeScreen(
                    userRole = "USER",
                    onEventClick = { },
                    showSnackbar = { },
                    viewModel = homeViewModel,
                    adminViewModel = AdminViewModel(FakeAdminRepository())
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Descending").assertIsSelected()

        composeTestRule.onNodeWithText("Ascending").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Ascending").assertIsSelected()
    }

    private fun sampleEvents(): List<EventResponse> {
        return listOf(
            EventResponse(
                id = 1L,
                title = "Alpha Concert",
                description = null,
                date = "2026-04-12T10:00:00",
                location = "Main Hall",
                category = EventCategory.CONCERT,
                capacity = 100,
                price = BigDecimal("20.00"),
                availableSeats = 40
            ),
            EventResponse(
                id = 2L,
                title = "Beta Movie",
                description = null,
                date = "2026-04-10T10:00:00",
                location = "Cinema",
                category = EventCategory.MOVIE,
                capacity = 80,
                price = BigDecimal("15.00"),
                availableSeats = 30
            ),
            EventResponse(
                id = 3L,
                title = "Alpha Expo",
                description = null,
                date = "2026-04-11T10:00:00",
                location = "Expo Center",
                category = EventCategory.CONCERT,
                capacity = 60,
                price = BigDecimal("10.00"),
                availableSeats = 20
            )
        )
    }

    private class FakeEventRepository(
        private val events: List<EventResponse>
    ) : EventRepository() {
        override suspend fun getAllEvents(): Result<List<EventResponse>> = Result.success(events)
    }

    private class FakeAdminRepository : AdminRepository()
}