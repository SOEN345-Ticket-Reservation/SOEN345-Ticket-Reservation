package com.soen345.ticketreservation.unit.service;

import com.soen345.ticketreservation.dto.request.ReservationRequest;
import com.soen345.ticketreservation.dto.response.ReservationResponse;
import com.soen345.ticketreservation.exception.EventFullException;
import com.soen345.ticketreservation.exception.ResourceNotFoundException;
import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.Reservation;
import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.model.enums.ReservationStatus;
import com.soen345.ticketreservation.model.enums.UserRole;
import com.soen345.ticketreservation.repository.EventRepository;
import com.soen345.ticketreservation.repository.ReservationRepository;
import com.soen345.ticketreservation.repository.UserRepository;
import com.soen345.ticketreservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    private User testUser;
    private Event testEvent;
    private Reservation testReservation;
    private ReservationRequest reservationRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encoded_password")
                .role(UserRole.CUSTOMER)
                .build();

        testEvent = Event.builder()
                .id(1L)
                .title("Summer Concert")
                .date(LocalDateTime.of(2026, 6, 15, 20, 0))
                .location("Montreal")
                .category(EventCategory.CONCERT)
                .capacity(500)
                .price(new BigDecimal("49.99"))
                .build();

        testReservation = Reservation.builder()
                .id(1L)
                .user(testUser)
                .event(testEvent)
                .status(ReservationStatus.CONFIRMED)
                .reservedAt(LocalDateTime.now())
                .confirmationCode("ABC12345")
                .build();

        reservationRequest = ReservationRequest.builder()
                .eventId(1L)
                .numberOfTickets(1)
                .build();
    }

    @Test
    void reserve_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(reservationRepository.findByEventId(1L)).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        ReservationResponse response = reservationService.reserve(1L, reservationRequest);

        assertNotNull(response);
        assertEquals(ReservationStatus.CONFIRMED, response.getStatus());
        assertEquals("ABC12345", response.getConfirmationCode());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void reserve_EventFull_ThrowsException() {
        testEvent.setCapacity(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(reservationRepository.findByEventId(1L)).thenReturn(List.of(testReservation));

        assertThrows(EventFullException.class,
                () -> reservationService.reserve(1L, reservationRequest));
    }

    @Test
    void reserve_UserNotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reservationService.reserve(99L, reservationRequest));
    }

    @Test
    void cancel_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        ReservationResponse response = reservationService.cancel(1L);

        assertNotNull(response);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void cancel_NotFound_ThrowsException() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reservationService.cancel(99L));
    }

    @Test
    void getUserReservations_ReturnsList() {
        when(reservationRepository.findByUserId(1L)).thenReturn(List.of(testReservation));

        List<ReservationResponse> reservations = reservationService.getUserReservations(1L);

        assertEquals(1, reservations.size());
        assertEquals("Summer Concert", reservations.get(0).getEventTitle());
    }
}
