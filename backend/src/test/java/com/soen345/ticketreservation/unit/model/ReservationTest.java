package com.soen345.ticketreservation.unit.model;

import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.Reservation;
import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.ReservationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void testReservationBuilder() {
        User user = User.builder().id(1L).name("John").email("john@test.com").password("pass").build();
        Event event = Event.builder().id(1L).title("Concert").build();

        Reservation reservation = Reservation.builder()
                .id(1L)
                .user(user)
                .event(event)
                .status(ReservationStatus.CONFIRMED)
                .confirmationCode("ABC12345")
                .build();

        assertEquals(1L, reservation.getId());
        assertEquals(user, reservation.getUser());
        assertEquals(event, reservation.getEvent());
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertEquals("ABC12345", reservation.getConfirmationCode());
        assertNotNull(reservation.getReservedAt());
    }

    @Test
    void testDefaultStatus() {
        Reservation reservation = Reservation.builder()
                .confirmationCode("XYZ99999")
                .build();

        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void testCancelReservation() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .status(ReservationStatus.CONFIRMED)
                .confirmationCode("ABC12345")
                .build();

        reservation.setStatus(ReservationStatus.CANCELLED);

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }
}
