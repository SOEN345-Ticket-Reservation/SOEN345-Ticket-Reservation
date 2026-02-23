package com.soen345.ticketreservation.dto.request;

import com.soen345.ticketreservation.model.enums.EventCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Category is required")
    private EventCategory category;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @NotNull(message = "Price is required")
    private BigDecimal price;
}
