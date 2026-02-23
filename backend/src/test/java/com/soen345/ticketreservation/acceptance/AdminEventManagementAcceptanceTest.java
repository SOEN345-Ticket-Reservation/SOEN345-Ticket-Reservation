package com.soen345.ticketreservation.acceptance;

import tools.jackson.databind.ObjectMapper;
import com.soen345.ticketreservation.dto.request.CreateEventRequest;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.repository.EventRepository;
import com.soen345.ticketreservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
@WithMockUser(roles = "ADMIN")
class AdminEventManagementAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    void fullFlow_AddEvent_EditEvent_DeleteEvent() throws Exception {
        // Step 1: Create a new event
        CreateEventRequest createRequest = CreateEventRequest.builder()
                .title("Jazz Night")
                .description("Smooth jazz evening")
                .date(LocalDateTime.of(2026, 9, 10, 20, 0))
                .location("Ottawa")
                .category(EventCategory.CONCERT)
                .capacity(300)
                .price(new BigDecimal("55.00"))
                .build();

        String createResponse = mockMvc.perform(post("/api/admin/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Jazz Night"))
                .andExpect(jsonPath("$.location").value("Ottawa"))
                .andReturn().getResponse().getContentAsString();

        Long eventId = objectMapper.readTree(createResponse).get("id").asLong();

        // Step 2: Update the event
        CreateEventRequest updateRequest = CreateEventRequest.builder()
                .title("Jazz Night - Updated")
                .description("Updated smooth jazz evening")
                .date(LocalDateTime.of(2026, 9, 10, 21, 0))
                .location("Ottawa Convention Centre")
                .category(EventCategory.CONCERT)
                .capacity(400)
                .price(new BigDecimal("65.00"))
                .build();

        mockMvc.perform(put("/api/admin/events/" + eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Jazz Night - Updated"))
                .andExpect(jsonPath("$.capacity").value(400));

        // Step 3: Verify event is updated in public listing
        mockMvc.perform(get("/api/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Jazz Night - Updated"));

        // Step 4: Delete the event
        mockMvc.perform(delete("/api/admin/events/" + eventId))
                .andExpect(status().isNoContent());

        // Step 5: Verify event is gone
        mockMvc.perform(get("/api/events/" + eventId))
                .andExpect(status().isNotFound());
    }
}
