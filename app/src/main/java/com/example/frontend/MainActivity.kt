package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.frontend.auth.TokenStorage
import com.example.frontend.model.UserResponse
import com.example.frontend.ui.auth.LoginScreen
import com.example.frontend.ui.auth.RegisterScreen
import com.example.frontend.ui.theme.FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenStorage.init(applicationContext)
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

@PreviewScreenSizes
@Composable
fun FrontendApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
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