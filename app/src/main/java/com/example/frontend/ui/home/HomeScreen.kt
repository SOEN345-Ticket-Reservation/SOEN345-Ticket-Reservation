package com.example.frontend.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.ui.components.ListItemCard

@Composable
fun HomeScreen(
    userRole: String,
    onEventClick: (Long) -> Unit,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel()
) {
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val dateSortOrder by viewModel.dateSortOrder.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<EventResponse?>(null) }
    var eventToDelete by remember { mutableStateOf<EventResponse?>(null) }

    val isAdmin = userRole.equals("ADMIN", ignoreCase = true)

    LaunchedEffect(Unit) { viewModel.loadEvents() }

    if (showAddDialog) {
        AddEditEventDialog(
            existingEvent = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { request ->
                adminViewModel.createEvent(request,
                    onSuccess = {
                        showAddDialog = false
                        viewModel.loadEvents()
                    },
                    onError = { showSnackbar(it) }
                )
            }
        )
    }

    eventToDelete?.let { deletingEvent ->
        AlertDialog(
            onDismissRequest = { eventToDelete = null },
            title = { Text("Delete Event") },
            text = { Text("Are you sure you want to delete \"${deletingEvent.title}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    adminViewModel.deleteEvent(deletingEvent.id,
                        onSuccess = {
                            eventToDelete = null
                            viewModel.loadEvents()
                        },
                        onError = { showSnackbar(it) }
                    )
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { eventToDelete = null }) { Text("Cancel") }
            }
        )
    }

    eventToEdit?.let { editingEvent ->
        AddEditEventDialog(
            existingEvent = editingEvent,
            onDismiss = { eventToEdit = null },
            onConfirm = { request ->
                adminViewModel.updateEvent(editingEvent.id, request,
                    onSuccess = {
                        eventToEdit = null
                        viewModel.loadEvents()
                    },
                    onError = { showSnackbar(it) }
                )
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                label = { Text("Search events...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = dateSortOrder == DateSortOrder.ASCENDING,
                        onClick = { viewModel.dateSortOrder.value = DateSortOrder.ASCENDING },
                        label = { Text("Ascending") }
                    )
                }
                item {
                    FilterChip(
                        selected = dateSortOrder == DateSortOrder.DESCENDING,
                        onClick = { viewModel.dateSortOrder.value = DateSortOrder.DESCENDING },
                        label = { Text("Descending") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.selectedCategory.value = null },
                        label = { Text("All") }
                    )
                }
                items(EventCategory.entries) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = {
                            viewModel.selectedCategory.value = if (selectedCategory == cat) null else cat
                        },
                        label = { Text(cat.name) }
                    )
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when {
                    isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                    error != null -> Text(
                        text = error ?: "Error",
                        modifier = Modifier.align(Alignment.Center)
                    )
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(events, key = { it.id }) { event ->
                            ListItemCard(
                                title = event.title,
                                subtitle = "${event.date} · ${event.location} · ${event.availableSeats} seats",
                                onClick = { onEventClick(event.id) },
                                trailingContent = {
                                    if (isAdmin) {
                                        IconButton(onClick = { eventToEdit = event }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = { eventToDelete = event }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        if (isAdmin) {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    }
}
