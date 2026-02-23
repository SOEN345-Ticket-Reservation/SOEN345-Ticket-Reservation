package com.soen345.ticketreservation.unit.model;

import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.enums.EventCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void testEventBuilder() {
        LocalDateTime eventDate = LocalDateTime.of(2026, 6, 15, 20, 0);

        Event event = Event.builder()
                .id(1L)
                .title("Summer Concert")
                .description("A great summer concert")
                .date(eventDate)
                .location("Montreal")
                .category(EventCategory.CONCERT)
                .capacity(500)
                .price(new BigDecimal("49.99"))
                .build();

        assertEquals(1L, event.getId());
        assertEquals("Summer Concert", event.getTitle());
        assertEquals("A great summer concert", event.getDescription());
        assertEquals(eventDate, event.getDate());
        assertEquals("Montreal", event.getLocation());
        assertEquals(EventCategory.CONCERT, event.getCategory());
        assertEquals(500, event.getCapacity());
        assertEquals(new BigDecimal("49.99"), event.getPrice());
    }

    @Test
    void testSettersAndGetters() {
        Event event = new Event();
        event.setId(2L);
        event.setTitle("Movie Night");
        event.setCategory(EventCategory.MOVIE);
        event.setCapacity(200);
        event.setPrice(new BigDecimal("15.00"));

        assertEquals(2L, event.getId());
        assertEquals("Movie Night", event.getTitle());
        assertEquals(EventCategory.MOVIE, event.getCategory());
        assertEquals(200, event.getCapacity());
        assertEquals(new BigDecimal("15.00"), event.getPrice());
    }
}
