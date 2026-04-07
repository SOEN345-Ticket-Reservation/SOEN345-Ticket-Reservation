package com.example.frontend.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.frontend.model.CreateEventRequest
import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.ui.theme.FrontendTheme
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@Suppress("TestFunctionName")
class AddEditEventDialogTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun AddEditEventDialog_displaysFieldsAndCategoryChips() {
        composeTestRule.setContent {
            FrontendTheme {
                AddEditEventDialog(
                    onDismiss = { },
                    onConfirm = { }
                )
            }
        }

        composeTestRule.onNodeWithText("Add Event").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date (yyyy-MM-ddTHH:mm:ss)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Location").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("MOVIE").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun AddEditEventDialog_saveEmitsFilledRequest() {
        val existingEvent = EventResponse(
            id = 4L,
            title = "Old Title",
            description = "Old description",
            date = "2026-04-12T10:00:00",
            location = "Old Hall",
            category = EventCategory.MOVIE,
            capacity = 50,
            price = BigDecimal("12.50"),
            availableSeats = 10
        )
        var capturedRequest: CreateEventRequest? = null

        composeTestRule.setContent {
            FrontendTheme {
                AddEditEventDialog(
                    existingEvent = existingEvent,
                    onDismiss = { },
                    onConfirm = { capturedRequest = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitForIdle()

        assertNotNull(capturedRequest)
        assertEquals(existingEvent.title, capturedRequest?.title)
        assertEquals(existingEvent.description, capturedRequest?.description)
        assertEquals(existingEvent.date, capturedRequest?.date)
        assertEquals(existingEvent.location, capturedRequest?.location)
        assertEquals(existingEvent.category, capturedRequest?.category)
        assertEquals(existingEvent.capacity, capturedRequest?.capacity)
        assertEquals(existingEvent.price, capturedRequest?.price)
    }
}