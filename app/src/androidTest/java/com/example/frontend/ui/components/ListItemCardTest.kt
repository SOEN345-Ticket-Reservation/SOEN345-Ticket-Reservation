package com.example.frontend.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.frontend.ui.theme.FrontendTheme
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for ListItemCard: title, subtitle, trailing content, and click.
 */
@Suppress("TestFunctionName")
class ListItemCardTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun ListItemCard_displaysTitleAndSubtitle() {
        composeTestRule.setContent {
            FrontendTheme {
                ListItemCard(
                    title = "Event Title",
                    subtitle = "2026-01-15 · Montreal"
                )
            }
        }
        composeTestRule.onNodeWithText("Event Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("2026-01-15 · Montreal").assertIsDisplayed()
    }

    @Test
    fun ListItemCard_withTrailingContent_displaysIt() {
        composeTestRule.setContent {
            FrontendTheme {
                ListItemCard(
                    title = "Reservation",
                    subtitle = "CONFIRMED",
                    trailingContent = { androidx.compose.material3.Text("Cancel") }
                )
            }
        }
        composeTestRule.onNodeWithText("Reservation").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun ListItemCard_withOnClick_invokesCallback() {
        var clicked = false
        composeTestRule.setContent {
            FrontendTheme {
                ListItemCard(
                    title = "Tap me",
                    subtitle = "Subtitle",
                    onClick = { clicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Tap me").performClick()
        composeTestRule.waitForIdle()
        assert(clicked)
    }
}
