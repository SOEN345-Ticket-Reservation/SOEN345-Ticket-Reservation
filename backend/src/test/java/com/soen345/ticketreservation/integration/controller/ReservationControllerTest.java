package com.soen345.ticketreservation.integration.controller;

import tools.jackson.databind.ObjectMapper;
import com.soen345.ticketreservation.dto.request.ReservationRequest;
import com.soen345.ticketreservation.dto.response.ReservationResponse;
import com.soen345.ticketreservation.model.enums.ReservationStatus;
import com.soen345.ticketreservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void createReservation_Success() throws Exception {
        ReservationRequest request = ReservationRequest.builder()
                .eventId(1L)
                .numberOfTickets(1)
                .build();

        ReservationResponse response = ReservationResponse.builder()
                .id(1L)
                .userId(1L)
                .userName("John Doe")
                .eventId(1L)
                .eventTitle("Summer Concert")
                .status(ReservationStatus.CONFIRMED)
                .reservedAt(LocalDateTime.now())
                .confirmationCode("ABC12345")
                .build();

        when(reservationService.reserve(eq(1L), any(ReservationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/reservations")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.confirmationCode").value("ABC12345"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void cancelReservation_Success() throws Exception {
        ReservationResponse response = ReservationResponse.builder()
                .id(1L)
                .status(ReservationStatus.CANCELLED)
                .confirmationCode("ABC12345")
                .build();

        when(reservationService.cancel(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void getUserReservations_Success() throws Exception {
        ReservationResponse response = ReservationResponse.builder()
                .id(1L)
                .userId(1L)
                .userName("John Doe")
                .eventId(1L)
                .eventTitle("Summer Concert")
                .status(ReservationStatus.CONFIRMED)
                .confirmationCode("ABC12345")
                .build();

        when(reservationService.getUserReservations(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/reservations/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventTitle").value("Summer Concert"));
    }
}
