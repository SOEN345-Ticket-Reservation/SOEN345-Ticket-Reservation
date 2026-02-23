package com.soen345.ticketreservation.acceptance;

import tools.jackson.databind.ObjectMapper;
import com.soen345.ticketreservation.dto.request.ReservationRequest;
import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.model.enums.UserRole;
import com.soen345.ticketreservation.repository.EventRepository;
import com.soen345.ticketreservation.repository.ReservationRepository;
import com.soen345.ticketreservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class TicketBookingAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .name("Bob Johnson")
                .email("bob@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(UserRole.CUSTOMER)
                .build());

        testEvent = eventRepository.save(Event.builder()
                .title("Rock Concert")
                .description("Amazing rock concert")
                .date(LocalDateTime.of(2026, 8, 20, 19, 0))
                .location("Montreal")
                .category(EventCategory.CONCERT)
                .capacity(100)
                .price(new BigDecimal("75.00"))
                .build());
    }

    @Test
    void fullFlow_BrowseEvents_Reserve_Confirm() throws Exception {
        // Step 1: Browse available events
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Rock Concert"));

        // Step 2: View event details
        mockMvc.perform(get("/api/events/" + testEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Rock Concert"))
                .andExpect(jsonPath("$.availableSeats").value(100));

        // Step 3: Reserve a ticket
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .eventId(testEvent.getId())
                .numberOfTickets(1)
                .build();

        mockMvc.perform(post("/api/reservations")
                .param("userId", testUser.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.eventTitle").value("Rock Concert"))
                .andExpect(jsonPath("$.confirmationCode").exists());

        // Step 4: Check user reservations
        mockMvc.perform(get("/api/reservations/user/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventTitle").value("Rock Concert"));
    }

    @Test
    void bookAndCancel_Flow() throws Exception {
        // Step 1: Reserve a ticket
        ReservationRequest request = ReservationRequest.builder()
                .eventId(testEvent.getId())
                .numberOfTickets(1)
                .build();

        String responseJson = mockMvc.perform(post("/api/reservations")
                .param("userId", testUser.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long reservationId = objectMapper.readTree(responseJson).get("id").asLong();

        // Step 2: Cancel the reservation
        mockMvc.perform(delete("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
