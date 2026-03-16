package com.example.frontend.ui.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.frontend.ui.theme.FrontendTheme
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for LoginScreen: display and Register link.
 */
@Suppress("TestFunctionName")
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun LoginScreen_displaysTitle_fields_andRegisterLink() {
        composeTestRule.setContent {
            FrontendTheme {
                LoginScreen(
                    onLoginSuccess = { },
                    onNavigateToRegister = { }
                )
            }
        }
        composeTestRule.onNodeWithText("Email or phone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Register").assertIsDisplayed()
    }

    @Test
    fun LoginScreen_clickRegisterLink_invokesCallback() {
        var navigatedToRegister = false
        composeTestRule.setContent {
            FrontendTheme {
                LoginScreen(
                    onLoginSuccess = { },
                    onNavigateToRegister = { navigatedToRegister = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Register").performClick()
        composeTestRule.waitForIdle()
        assert(navigatedToRegister)
    }
}
