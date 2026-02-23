package com.soen345.ticketreservation.integration.repository;

import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.repository.EventRepository;
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
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();

        Event concert = Event.builder()
                .title("Summer Concert")
                .description("Great concert")
                .date(LocalDateTime.of(2026, 6, 15, 20, 0))
                .location("Montreal")
                .category(EventCategory.CONCERT)
                .capacity(500)
                .price(new BigDecimal("49.99"))
                .build();

        Event movie = Event.builder()
                .title("Movie Night")
                .description("New release")
                .date(LocalDateTime.of(2026, 7, 1, 19, 0))
                .location("Toronto")
                .category(EventCategory.MOVIE)
                .capacity(200)
                .price(new BigDecimal("15.00"))
                .build();

        eventRepository.save(concert);
        eventRepository.save(movie);
    }

    @Test
    void findByCategory_Concert() {
        List<Event> events = eventRepository.findByCategory(EventCategory.CONCERT);
        assertEquals(1, events.size());
        assertEquals("Summer Concert", events.get(0).getTitle());
    }

    @Test
    void findByCategory_NoResults() {
        List<Event> events = eventRepository.findByCategory(EventCategory.SPORTS);
        assertTrue(events.isEmpty());
    }

    @Test
    void findByLocation() {
        List<Event> events = eventRepository.findByLocation("Montreal");
        assertEquals(1, events.size());
        assertEquals("Summer Concert", events.get(0).getTitle());
    }

    @Test
    void findByDateBetween() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 30, 23, 59);

        List<Event> events = eventRepository.findByDateBetween(start, end);
        assertEquals(1, events.size());
        assertEquals("Summer Concert", events.get(0).getTitle());
    }

    @Test
    void findByDateBetween_BothEvents() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 12, 31, 23, 59);

        List<Event> events = eventRepository.findByDateBetween(start, end);
        assertEquals(2, events.size());
    }
}
