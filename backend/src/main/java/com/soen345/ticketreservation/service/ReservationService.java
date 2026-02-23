package com.soen345.ticketreservation.service;

import com.soen345.ticketreservation.dto.request.ReservationRequest;
import com.soen345.ticketreservation.dto.response.ReservationResponse;
import com.soen345.ticketreservation.exception.EventFullException;
import com.soen345.ticketreservation.exception.ResourceNotFoundException;
import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.Reservation;
import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.ReservationStatus;
import com.soen345.ticketreservation.repository.EventRepository;
import com.soen345.ticketreservation.repository.ReservationRepository;
import com.soen345.ticketreservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReservationResponse reserve(Long userId, ReservationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + request.getEventId()));

        long confirmedCount = reservationRepository.findByEventId(event.getId()).stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .count();

        if (confirmedCount + request.getNumberOfTickets() > event.getCapacity()) {
            throw new EventFullException("Not enough seats available for event: " + event.getTitle());
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .event(event)
                .status(ReservationStatus.CONFIRMED)
                .reservedAt(LocalDateTime.now())
                .confirmationCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        Reservation saved = reservationRepository.save(reservation);
        return mapToResponse(saved);
    }

    @Transactional
    public ReservationResponse cancel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation updated = reservationRepository.save(reservation);
        return mapToResponse(updated);
    }

    public List<ReservationResponse> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ReservationResponse getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return mapToResponse(reservation);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .userName(reservation.getUser().getName())
                .eventId(reservation.getEvent().getId())
                .eventTitle(reservation.getEvent().getTitle())
                .status(reservation.getStatus())
                .reservedAt(reservation.getReservedAt())
                .confirmationCode(reservation.getConfirmationCode())
                .build();
    }
}
