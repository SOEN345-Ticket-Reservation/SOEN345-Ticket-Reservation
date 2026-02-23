package com.soen345.ticketreservation.service;

import com.soen345.ticketreservation.dto.request.CreateEventRequest;
import com.soen345.ticketreservation.dto.response.EventResponse;
import com.soen345.ticketreservation.exception.ResourceNotFoundException;
import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.enums.EventCategory;
import com.soen345.ticketreservation.model.enums.ReservationStatus;
import com.soen345.ticketreservation.repository.EventRepository;
import com.soen345.ticketreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return mapToResponse(event);
    }

    public List<EventResponse> getEventsByCategory(EventCategory category) {
        return eventRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByLocation(String location) {
        return eventRepository.findByLocation(location).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByDateRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByDateBetween(start, end).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse createEvent(CreateEventRequest request) {
        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(request.getDate())
                .location(request.getLocation())
                .category(request.getCategory())
                .capacity(request.getCapacity())
                .price(request.getPrice())
                .build();

        Event savedEvent = eventRepository.save(event);
        return mapToResponse(savedEvent);
    }

    public EventResponse updateEvent(Long id, CreateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDate(request.getDate());
        event.setLocation(request.getLocation());
        event.setCategory(request.getCategory());
        event.setCapacity(request.getCapacity());
        event.setPrice(request.getPrice());

        Event updatedEvent = eventRepository.save(event);
        return mapToResponse(updatedEvent);
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    public int getAvailableSeats(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        long reservedCount = reservationRepository.findByEventId(eventId).stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .count();

        return event.getCapacity() - (int) reservedCount;
    }

    private EventResponse mapToResponse(Event event) {
        int availableSeats = event.getCapacity() - (int) reservationRepository.findByEventId(event.getId()).stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .count();

        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .date(event.getDate())
                .location(event.getLocation())
                .category(event.getCategory())
                .capacity(event.getCapacity())
                .price(event.getPrice())
                .availableSeats(availableSeats)
                .build();
    }
}
