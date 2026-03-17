package com.example.frontend.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend.auth.TokenStorage
import com.example.frontend.repository.EventRepository
import com.example.frontend.repository.ReservationRepository
import kotlinx.coroutines.launch

@Composable
fun EventDetailScreen(
    eventId: Long,
    onBack: () -> Unit,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    eventRepository: EventRepository = EventRepository(),
    reservationRepository: ReservationRepository = ReservationRepository()
) {
    var event by remember { mutableStateOf<com.example.frontend.model.EventResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var reserveLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(eventId) {
        isLoading = true
        eventRepository.getEventById(eventId)
            .onSuccess { event = it }
            .onFailure { showSnackbar(it.message ?: "Failed to load event") }
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }
        if (isLoading || event == null) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else {
            val e = event!!
            Text(text = e.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(text = e.description ?: "", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(text = "Date: ${e.date}")
            Text(text = "Location: ${e.location}")
            Text(text = "Price: $${e.price}")
            Text(text = "Available seats: ${e.availableSeats}")
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    reserveLoading = true
                    scope.launch {
                        val userId = TokenStorage.getUserId()
                        if (userId == null) {
                            showSnackbar("Not logged in")
                            reserveLoading = false
                            return@launch
                        }
                        reservationRepository.createReservation(userId, eventId, 1)
                            .onSuccess {
                                showSnackbar("Reserved!")
                            }
                            .onFailure {
                                showSnackbar(it.message ?: "Reserve failed")
                            }
                        reserveLoading = false
                    }
                },
                enabled = !reserveLoading && e.availableSeats > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (reserveLoading) CircularProgressIndicator(Modifier.height(24.dp))
                else Text("Reserve")
            }
        }
    }
}
