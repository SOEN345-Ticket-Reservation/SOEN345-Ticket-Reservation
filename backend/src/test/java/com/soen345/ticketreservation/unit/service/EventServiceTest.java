package com.soen345.ticketreservation.unit.service;

import com.soen345.ticketreservation.dto.response.EventResponse;
import com.soen345.ticketreservation.exception.ResourceNotFoundException;
import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.repository.EventRepository;
import com.soen345.ticketreservation.repository.ReservationRepository;
import com.soen345.ticketreservation.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = Event.builder()
                .id(1L)
                .title("Summer Concert")
                .description("A great summer concert")
                .date(LocalDateTime.of(2026, 6, 15, 20, 0))
                .location("Montreal")
                .category(EventCategory.CONCERT)
                .capacity(500)
                .price(new BigDecimal("49.99"))
                .build();
    }

    @Test
    void getAllEvents_ReturnsList() {
        when(eventRepository.findAll()).thenReturn(List.of(testEvent));

        List<EventResponse> events = eventService.getAllEvents();

        assertEquals(1, events.size());
        assertEquals("Summer Concert", events.get(0).getTitle());
    }

    @Test
    void getEventById_Found() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        EventResponse response = eventService.getEventById(1L);

        assertNotNull(response);
        assertEquals("Summer Concert", response.getTitle());
        assertEquals(500, response.getAvailableSeats());
    }

    @Test
    void getEventById_NotFound_ThrowsException() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> eventService.getEventById(99L));
    }
}
