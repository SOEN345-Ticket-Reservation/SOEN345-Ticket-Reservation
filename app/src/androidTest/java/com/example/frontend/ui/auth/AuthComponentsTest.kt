package com.example.frontend.ui.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.frontend.ui.theme.FrontendTheme
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for auth UI components: AuthTextField, AuthButton, AuthFooter, AuthScreenLayout.
 */
@Suppress("TestFunctionName")
class AuthComponentsTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun AuthTextField_displaysLabel() {
        composeTestRule.setContent {
            FrontendTheme {
                AuthTextField(
                    value = "",
                    onValueChange = { },
                    label = "Email"
                )
            }
        }
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
    }

    @Test
    fun AuthButton_displaysText_andIsClickable() {
        var clicked = false
        composeTestRule.setContent {
            FrontendTheme {
                AuthButton(text = "Login", onClick = { clicked = true })
            }
        }
        composeTestRule.onNodeWithText("Login").assertIsDisplayed().assertIsEnabled()
        composeTestRule.onNodeWithText("Login").performClick()
        assert(clicked)
    }

    @Test
    fun AuthButton_whenDisabled_isNotClickable() {
        composeTestRule.setContent {
            FrontendTheme {
                AuthButton(text = "Submit", onClick = { }, enabled = false)
            }
        }
        composeTestRule.onNodeWithText("Submit").assertIsNotEnabled()
    }

    @Test
    fun AuthFooter_displaysTextAndLink_andLinkClickFiresCallback() {
        var linkClicked = false
        composeTestRule.setContent {
            FrontendTheme {
                AuthFooter(
                    text = "No account? ",
                    linkText = "Register",
                    onClickLink = { linkClicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("No account? ").assertIsDisplayed()
        composeTestRule.onNodeWithText("Register").assertIsDisplayed().performClick()
        assert(linkClicked)
    }

    @Test
    fun AuthScreenLayout_displaysTitle_andFormAndFooterContent() {
        composeTestRule.setContent {
            FrontendTheme {
                AuthScreenLayout(
                    title = "Sign in",
                    formContent = { AuthButton(text = "Submit", onClick = { }) },
                    footerContent = { AuthFooter("Footer ", "Link", onClickLink = { }) }
                )
            }
        }
        composeTestRule.onNodeWithText("Sign in").assertIsDisplayed()
        composeTestRule.onNodeWithText("Submit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Footer ").assertIsDisplayed()
        composeTestRule.onNodeWithText("Link").assertIsDisplayed()
    }
}
