package com.example.frontend.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend.model.CreateEventRequest
import com.example.frontend.model.EventCategory
import com.example.frontend.model.EventResponse
import com.example.frontend.ui.auth.AuthTextField
import java.math.BigDecimal

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditEventDialog(
    existingEvent: EventResponse? = null,
    onDismiss: () -> Unit,
    onConfirm: (CreateEventRequest) -> Unit
) {
    var title by remember { mutableStateOf(existingEvent?.title ?: "") }
    var description by remember { mutableStateOf(existingEvent?.description ?: "") }
    var date by remember { mutableStateOf(existingEvent?.date ?: "") }
    var location by remember { mutableStateOf(existingEvent?.location ?: "") }
    var category by remember { mutableStateOf(existingEvent?.category ?: EventCategory.MOVIE) }
    var capacity by remember { mutableStateOf(existingEvent?.capacity?.toString() ?: "") }
    var price by remember { mutableStateOf(existingEvent?.price?.toPlainString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existingEvent == null) "Add Event" else "Edit Event") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                AuthTextField(value = title, onValueChange = { title = it }, label = "Title")
                Spacer(Modifier.height(8.dp))
                AuthTextField(value = description, onValueChange = { description = it }, label = "Description")
                Spacer(Modifier.height(8.dp))
                AuthTextField(value = date, onValueChange = { date = it }, label = "Date (yyyy-MM-ddTHH:mm:ss)")
                Spacer(Modifier.height(8.dp))
                AuthTextField(value = location, onValueChange = { location = it }, label = "Location")
                Spacer(Modifier.height(8.dp))
                Text("Category", style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EventCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.name) }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                AuthTextField(value = capacity, onValueChange = { capacity = it }, label = "Capacity")
                Spacer(Modifier.height(8.dp))
                AuthTextField(value = price, onValueChange = { price = it }, label = "Price")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val capacityInt = capacity.toIntOrNull() ?: return@TextButton
                    val priceDec = price.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    onConfirm(
                        CreateEventRequest(
                            title = title.trim(),
                            description = description.trim().takeIf { it.isNotEmpty() },
                            date = date.trim(),
                            location = location.trim(),
                            category = category,
                            capacity = capacityInt,
                            price = priceDec
                        )
                    )
                },
                enabled = title.isNotBlank() && date.isNotBlank() && location.isNotBlank() && capacity.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
