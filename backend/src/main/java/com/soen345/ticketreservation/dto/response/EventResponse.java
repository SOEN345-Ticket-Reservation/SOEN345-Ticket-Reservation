package com.soen345.ticketreservation.dto.response;

import com.soen345.ticketreservation.model.enums.EventCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private String location;
    private EventCategory category;
    private int capacity;
    private BigDecimal price;
    private int availableSeats;
}
