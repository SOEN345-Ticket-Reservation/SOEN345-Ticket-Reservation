package com.soen345.ticketreservation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @Min(value = 1, message = "Number of tickets must be at least 1")
    @Builder.Default
    private int numberOfTickets = 1;
}
