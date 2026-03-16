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
 * Unit tests for RegisterScreen: display and Login link.
 */
@Suppress("TestFunctionName")
class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun RegisterScreen_displaysTitle_fields_andLoginLink() {
        composeTestRule.setContent {
            FrontendTheme {
                RegisterScreen(
                    onRegisterSuccess = { },
                    onNavigateToLogin = { }
                )
            }
        }
        composeTestRule.onNodeWithText("Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Phone (optional)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
    }

    @Test
    fun RegisterScreen_clickLoginLink_invokesCallback() {
        var navigatedToLogin = false
        composeTestRule.setContent {
            FrontendTheme {
                RegisterScreen(
                    onRegisterSuccess = { },
                    onNavigateToLogin = { navigatedToLogin = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.waitForIdle()
        assert(navigatedToLogin)
    }
}
