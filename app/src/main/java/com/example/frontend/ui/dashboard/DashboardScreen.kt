package com.example.frontend.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.home.EventDetailScreen
import com.example.frontend.ui.home.HomeScreen
import com.example.frontend.ui.reservations.ReservationsScreen
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    userName: String,
    userRole: String,
    onSignOut: () -> Unit,
    homeContent: @Composable (onEventClick: (Long) -> Unit, showSnackbar: (String) -> Unit) -> Unit = { onEventClick, showSnackbar ->
        HomeScreen(
            userRole = userRole,
            onEventClick = onEventClick,
            showSnackbar = showSnackbar
        )
    },
    reservationsContent: @Composable (showSnackbar: (String) -> Unit) -> Unit = { showSnackbar ->
        ReservationsScreen(showSnackbar = showSnackbar)
    }
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedEventId by remember { mutableStateOf<Long?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (selectedEventId != null) {
        EventDetailScreen(
            eventId = selectedEventId!!,
            onBack = { selectedEventId = null },
            showSnackbar = { message -> scope.launch { snackbarHostState.showSnackbar(message) } }
        )
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Events") },
                    label = { Text("Events") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Star, contentDescription = "My Reservations") },
                    label = { Text("My Reservations") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = if (userName.isBlank()) "User" else userName)
                    Text(
                        text = if (userRole.isBlank()) "Role: Unknown" else "Role: $userRole",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onSignOut) {
                    Text("SignOut")
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (selectedTab) {
                    0 -> homeContent(
                        { selectedEventId = it },
                        { message -> scope.launch { snackbarHostState.showSnackbar(message) } }
                    )
                    1 -> reservationsContent { message -> scope.launch { snackbarHostState.showSnackbar(message) } }
                }
            }
        }
    }
}
