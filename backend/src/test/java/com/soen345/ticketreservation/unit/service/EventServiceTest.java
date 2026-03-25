package com.soen345.ticketreservation.unit.service;

import com.soen345.ticketreservation.dto.response.EventResponse;
import com.soen345.ticketreservation.exception.ResourceNotFoundException;
import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.model.enums.ReservationStatus;
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

    @Test
    void getEventsByCategory_ReturnsList() {
        when(eventRepository.findByCategory(EventCategory.CONCERT)).thenReturn(List.of(testEvent));

        List<EventResponse> events = eventService.getEventsByCategory(EventCategory.CONCERT);

        assertEquals(1, events.size());
        assertEquals("Summer Concert", events.get(0).getTitle());
    }

    @Test
    void getEventsByLocation_ReturnsList() {
        when(eventRepository.findByLocation("Montreal")).thenReturn(List.of(testEvent));

        List<EventResponse> events = eventService.getEventsByLocation("Montreal");

        assertEquals(1, events.size());
        assertEquals("Summer Concert", events.get(0).getTitle());
    }

    @Test
    void getEventsByDateRange_ReturnsList() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 30, 23, 59);
        when(eventRepository.findByDateBetween(start, end)).thenReturn(List.of(testEvent));

        List<EventResponse> events = eventService.getEventsByDateRange(start, end);

        assertEquals(1, events.size());
        assertEquals("Summer Concert", events.get(0).getTitle());
    }

    @Test
    void getAvailableSeats_ReturnsCorrectCount() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(reservationRepository.countByEventIdAndStatus(1L, ReservationStatus.CONFIRMED)).thenReturn(100L);

        int availableSeats = eventService.getAvailableSeats(1L);

        assertEquals(400, availableSeats);
    }

    @Test
    void getAvailableSeats_EventNotFound_ThrowsException() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> eventService.getAvailableSeats(99L));
    }
}
