package com.example.frontend.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    userName: String,
    userRole: String,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(text = if (userName.isBlank()) "User" else userName)
                Text(
                    text = if (userRole.isBlank()) "Role: Unknown" else "Role: $userRole",
                    style = MaterialTheme.typography.bodySmall
                )
                Button(onClick = onSignOut) {
                    Text("SignOut")
                }
            }
        }

        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Start
        )
    }
}
