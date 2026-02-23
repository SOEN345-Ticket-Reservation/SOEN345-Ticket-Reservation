package com.soen345.ticketreservation.dto.response;

import com.soen345.ticketreservation.model.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Long eventId;
    private String eventTitle;
    private ReservationStatus status;
    private LocalDateTime reservedAt;
    private String confirmationCode;
}
