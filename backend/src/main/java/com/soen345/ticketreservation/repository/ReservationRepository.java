package com.soen345.ticketreservation.repository;

import com.soen345.ticketreservation.model.Reservation;
import com.soen345.ticketreservation.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByEventId(Long eventId);

    long countByEventIdAndStatus(Long eventId, ReservationStatus status);
}
