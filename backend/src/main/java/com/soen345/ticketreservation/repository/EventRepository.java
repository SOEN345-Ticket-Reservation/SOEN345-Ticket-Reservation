package com.soen345.ticketreservation.repository;

import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.enums.EventCategory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCategory(EventCategory category);

    List<Event> findByLocation(String location);

    List<Event> findByDateBetween(LocalDateTime start, LocalDateTime end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdWithLock(@Param("id") Long id);
}
