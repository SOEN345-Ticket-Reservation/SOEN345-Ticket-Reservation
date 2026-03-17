package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.frontend.auth.ReservationNotificationPrefs
import com.example.frontend.auth.TokenStorage
import com.example.frontend.model.UserResponse
import com.example.frontend.ui.auth.LoginScreen
import com.example.frontend.ui.auth.RegisterScreen
import com.example.frontend.ui.dashboard.DashboardScreen
import com.example.frontend.ui.dashboard.SessionUiState
import com.example.frontend.ui.dashboard.onAuthSuccess
import com.example.frontend.ui.dashboard.onSignOut
import com.example.frontend.ui.theme.FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenStorage.init(applicationContext)
        ReservationNotificationPrefs.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            FrontendTheme {
                RootContent()
            }
        }
    }
}

@Composable
private fun RootContent() {
    val sessionState = remember {
        mutableStateOf(
            SessionUiState(
                isLoggedIn = TokenStorage.getToken() != null,
                userName = "",
                userRole = ""
            )
        )
    }

    if (sessionState.value.isLoggedIn) {
        DashboardScreen(
            userName = sessionState.value.userName,
            userRole = sessionState.value.userRole,
            onSignOut = {
                TokenStorage.clearToken()
                sessionState.value = onSignOut()
            }
        )
    } else {
        AuthFlow(
            onLoginSuccess = { response: UserResponse ->
                response.token?.let { TokenStorage.setToken(it) }
                TokenStorage.setUserId(response.id)
                sessionState.value = onAuthSuccess(response)
            }
        )
    }
}

@Composable
private fun AuthFlow(onLoginSuccess: (UserResponse) -> Unit) {
    var showRegister by rememberSaveable { mutableStateOf(false) }

    if (showRegister) {
        RegisterScreen(
            onRegisterSuccess = onLoginSuccess,
            onNavigateToLogin = { showRegister = false }
        )
    } else {
        LoginScreen(
            onLoginSuccess = onLoginSuccess,
            onNavigateToRegister = { showRegister = true }
        )
    }
}