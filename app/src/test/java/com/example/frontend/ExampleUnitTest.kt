package com.example.frontend

import com.example.frontend.model.UserResponse
import com.example.frontend.ui.dashboard.onAuthSuccess
import com.example.frontend.ui.dashboard.onSignOut
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class ExampleUnitTest {
    @Test
    fun onAuthSuccess_setsLoggedInStateWithNameAndRole() {
        val user = UserResponse(
            id = 1L,
            name = "Sam",
            email = "sam@example.com",
            phone = null,
            role = "USER",
            token = "token-123"
        )

        val state = onAuthSuccess(user)

        assertTrue(state.isLoggedIn)
        assertEquals("Sam", state.userName)
        assertEquals("USER", state.userRole)
    }

    @Test
    fun onSignOut_resetsToLoggedOutState() {
        val state = onSignOut()

        assertFalse(state.isLoggedIn)
        assertEquals("", state.userName)
        assertEquals("", state.userRole)
    }
}