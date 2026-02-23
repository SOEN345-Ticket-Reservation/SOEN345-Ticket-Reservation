package com.soen345.ticketreservation.integration.repository;

import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.Reservation;
import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.model.enums.ReservationStatus;
import com.soen345.ticketreservation.model.enums.UserRole;
import com.soen345.ticketreservation.repository.EventRepository;
import com.soen345.ticketreservation.repository.ReservationRepository;
import com.soen345.ticketreservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("encoded_password")
                .role(UserRole.CUSTOMER)
                .build());

        testEvent = eventRepository.save(Event.builder()
                .title("Concert")
                .description("Great concert")
                .date(LocalDateTime.of(2026, 6, 15, 20, 0))
                .location("Montreal")
                .category(EventCategory.CONCERT)
                .capacity(500)
                .price(new BigDecimal("49.99"))
                .build());

        reservationRepository.save(Reservation.builder()
                .user(testUser)
                .event(testEvent)
                .status(ReservationStatus.CONFIRMED)
                .reservedAt(LocalDateTime.now())
                .confirmationCode("ABC12345")
                .build());
    }

    @Test
    void findByUserId() {
        List<Reservation> reservations = reservationRepository.findByUserId(testUser.getId());
        assertEquals(1, reservations.size());
        assertEquals("ABC12345", reservations.get(0).getConfirmationCode());
    }

    @Test
    void findByEventId() {
        List<Reservation> reservations = reservationRepository.findByEventId(testEvent.getId());
        assertEquals(1, reservations.size());
        assertEquals(ReservationStatus.CONFIRMED, reservations.get(0).getStatus());
    }

    @Test
    void findByUserId_NoResults() {
        List<Reservation> reservations = reservationRepository.findByUserId(999L);
        assertTrue(reservations.isEmpty());
    }
}
