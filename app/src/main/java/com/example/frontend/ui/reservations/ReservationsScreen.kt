package com.example.frontend.ui.reservations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.auth.ReservationNotificationPrefs
import com.example.frontend.model.ReservationStatus
import com.example.frontend.ui.components.ListItemCard

@Composable
fun ReservationsScreen(
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReservationsViewModel = viewModel()
) {
    val context = LocalContext.current
    remember { ReservationNotificationPrefs.init(context) }

    val reservations by viewModel.reservations.collectAsState()
    val notifyEnabledIds by viewModel.notifyEnabledIds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadReservations { showSnackbar(it) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            isLoading && reservations.isEmpty() -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            reservations.isEmpty() -> Text(
                text = "No reservations",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(reservations, key = { it.id }) { reservation ->
                    ListItemCard(
                        title = reservation.eventTitle,
                        subtitle = "${reservation.status} · Code: ${reservation.confirmationCode} · ${reservation.numberOfTickets} ticket(s)",
                        onClick = null,
                        trailingContent = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (reservation.status == ReservationStatus.CONFIRMED) {
                                    Button(
                                        onClick = {
                                            viewModel.cancelReservation(
                                                reservation.id,
                                                onSuccess = { showSnackbar("Reservation cancelled") },
                                                onError = { showSnackbar(it) }
                                            )
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Notify", style = MaterialTheme.typography.labelSmall)
                                    Switch(
                                        checked = reservation.id in notifyEnabledIds,
                                        onCheckedChange = { viewModel.setNotify(reservation.id, it) }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
