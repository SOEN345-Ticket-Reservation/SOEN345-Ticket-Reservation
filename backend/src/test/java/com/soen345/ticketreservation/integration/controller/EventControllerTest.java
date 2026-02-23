package com.soen345.ticketreservation.integration.controller;

import com.soen345.ticketreservation.dto.response.EventResponse;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    void getAllEvents_Success() throws Exception {
        EventResponse event = EventResponse.builder()
                .id(1L)
                .title("Summer Concert")
                .date(LocalDateTime.of(2026, 6, 15, 20, 0))
                .location("Montreal")
                .category(EventCategory.CONCERT)
                .capacity(500)
                .price(new BigDecimal("49.99"))
                .availableSeats(500)
                .build();

        when(eventService.getAllEvents()).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Summer Concert"))
                .andExpect(jsonPath("$[0].location").value("Montreal"));
    }

    @Test
    void getEventById_Success() throws Exception {
        EventResponse event = EventResponse.builder()
                .id(1L)
                .title("Summer Concert")
                .date(LocalDateTime.of(2026, 6, 15, 20, 0))
                .location("Montreal")
                .category(EventCategory.CONCERT)
                .capacity(500)
                .price(new BigDecimal("49.99"))
                .availableSeats(500)
                .build();

        when(eventService.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Summer Concert"));
    }

    @Test
    void getEventsByCategory_Success() throws Exception {
        EventResponse event = EventResponse.builder()
                .id(1L)
                .title("Summer Concert")
                .category(EventCategory.CONCERT)
                .build();

        when(eventService.getEventsByCategory(EventCategory.CONCERT)).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events/category/CONCERT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("CONCERT"));
    }
}
