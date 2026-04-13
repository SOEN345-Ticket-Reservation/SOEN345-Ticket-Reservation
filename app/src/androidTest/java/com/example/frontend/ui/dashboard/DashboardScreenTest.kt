package com.example.frontend.ui.dashboard

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.frontend.ui.theme.FrontendTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@Suppress("TestFunctionName")
class DashboardScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun DashboardScreen_displaysUserInfo_andSwitchesTabs() {
        composeTestRule.setContent {
            FrontendTheme {
                DashboardScreen(
                    userName = "Alex",
                    userRole = "ADMIN",
                    onSignOut = { },
                    homeContent = { _, _ -> Text("Home Slot") },
                    reservationsContent = { _ -> Text("Reservations Slot") }
                )
            }
        }

        composeTestRule.onNodeWithText("Alex").assertIsDisplayed()
        composeTestRule.onNodeWithText("Role: ADMIN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Home Slot").assertIsDisplayed()

        composeTestRule.onNodeWithText("My Reservations").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Reservations Slot").assertIsDisplayed()
    }

    @Test
    fun DashboardScreen_signOutButtonInvokesCallback() {
        var signOutClicked = false

        composeTestRule.setContent {
            FrontendTheme {
                DashboardScreen(
                    userName = "Alex",
                    userRole = "ADMIN",
                    onSignOut = { signOutClicked = true },
                    homeContent = { _, _ -> Text("Home Slot") },
                    reservationsContent = { _ -> Text("Reservations Slot") }
                )
            }
        }

        composeTestRule.onNodeWithText("SignOut").performClick()
        composeTestRule.waitForIdle()

        assertTrue(signOutClicked)
    }
}