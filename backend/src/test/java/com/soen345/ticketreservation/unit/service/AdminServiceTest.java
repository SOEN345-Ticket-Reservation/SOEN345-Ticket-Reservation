package com.soen345.ticketreservation.unit.service;

import com.soen345.ticketreservation.dto.request.CreateEventRequest;
import com.soen345.ticketreservation.dto.response.EventResponse;
import com.soen345.ticketreservation.exception.ResourceNotFoundException;
import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.repository.EventRepository;
import com.soen345.ticketreservation.repository.ReservationRepository;
import com.soen345.ticketreservation.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private AdminService adminService;

    private Event testEvent;
    private CreateEventRequest createEventRequest;

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

        createEventRequest = CreateEventRequest.builder()
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
    void createEvent_Success() {
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        when(reservationRepository.findByEventId(1L)).thenReturn(Collections.emptyList());

        EventResponse response = adminService.createEvent(createEventRequest);

        assertNotNull(response);
        assertEquals("Summer Concert", response.getTitle());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void updateEvent_Success() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        when(reservationRepository.findByEventId(1L)).thenReturn(Collections.emptyList());

        EventResponse response = adminService.updateEvent(1L, createEventRequest);

        assertNotNull(response);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void updateEvent_NotFound_ThrowsException() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateEvent(99L, createEventRequest));
    }

    @Test
    void deleteEvent_Success() {
        when(eventRepository.existsById(1L)).thenReturn(true);

        adminService.deleteEvent(1L);

        verify(eventRepository).deleteById(1L);
    }

    @Test
    void deleteEvent_NotFound_ThrowsException() {
        when(eventRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> adminService.deleteEvent(99L));
    }
}
