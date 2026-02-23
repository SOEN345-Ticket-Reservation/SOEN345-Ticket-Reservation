package com.soen345.ticketreservation.controller;

import com.soen345.ticketreservation.dto.response.EventResponse;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<EventResponse>> getEventsByCategory(@PathVariable EventCategory category) {
        return ResponseEntity.ok(eventService.getEventsByCategory(category));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<EventResponse>> getEventsByLocation(@PathVariable String location) {
        return ResponseEntity.ok(eventService.getEventsByLocation(location));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<EventResponse>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(eventService.getEventsByDateRange(start, end));
    }
}
