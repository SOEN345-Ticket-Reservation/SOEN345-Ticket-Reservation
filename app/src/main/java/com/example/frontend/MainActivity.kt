package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.frontend.auth.ReservationNotificationPrefs
import com.example.frontend.auth.TokenStorage
import com.example.frontend.model.UserResponse
import com.example.frontend.ui.auth.LoginScreen
import com.example.frontend.ui.auth.RegisterScreen
import com.example.frontend.ui.home.EventDetailScreen
import com.example.frontend.ui.home.HomeScreen
import com.example.frontend.ui.reservations.ReservationsScreen
import com.example.frontend.ui.theme.FrontendTheme
import kotlinx.coroutines.launch

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
    var isLoggedIn by remember { mutableStateOf(TokenStorage.getToken() != null) }
    if (isLoggedIn) {
        FrontendApp()
    } else {
        AuthFlow(
            onLoginSuccess = { response: UserResponse ->
                response.token?.let { TokenStorage.setToken(it) }
                TokenStorage.setUserId(response.id)
                isLoggedIn = true
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

private sealed class Screen {
    data object Home : Screen()
    data class EventDetail(val eventId: Long) : Screen()
    data object Reservations : Screen()
    data object Profile : Screen()
}

@PreviewScreenSizes
@Composable
fun FrontendApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showSnackbar: (String) -> Unit = { msg ->
        scope.launch {
            snackbarHostState.showSnackbar(msg)
        }
    }

    fun navDestination(): AppDestinations = when (currentScreen) {
        is Screen.Home, is Screen.EventDetail -> AppDestinations.HOME
        is Screen.Reservations -> AppDestinations.RESERVATIONS
        is Screen.Profile -> AppDestinations.PROFILE
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { dest ->
                item(
                    icon = { Icon(dest.icon, contentDescription = dest.label) },
                    label = { Text(dest.label) },
                    selected = navDestination() == dest,
                    onClick = {
                        currentScreen = when (dest) {
                            AppDestinations.HOME -> Screen.Home
                            AppDestinations.RESERVATIONS -> Screen.Reservations
                            AppDestinations.PROFILE -> Screen.Profile
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            when (val screen = currentScreen) {
                is Screen.Home -> HomeScreen(
                    onEventClick = { currentScreen = Screen.EventDetail(it) },
                    modifier = Modifier.padding(innerPadding)
                )
                is Screen.EventDetail -> EventDetailScreen(
                    eventId = screen.eventId,
                    onBack = { currentScreen = Screen.Home },
                    showSnackbar = showSnackbar,
                    modifier = Modifier.padding(innerPadding)
                )
                is Screen.Reservations -> ReservationsScreen(
                    showSnackbar = showSnackbar,
                    modifier = Modifier.padding(innerPadding).fillMaxSize()
                )
                is Screen.Profile -> Greeting(
                    name = "User",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    RESERVATIONS("Reservations", Icons.Default.ConfirmationNumber),
    PROFILE("Profile", Icons.Default.AccountBox),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontendTheme {
        Greeting("Android")
    }
}