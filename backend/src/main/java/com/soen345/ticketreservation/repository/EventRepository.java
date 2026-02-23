package com.soen345.ticketreservation.repository;

import com.soen345.ticketreservation.model.Event;
import com.soen345.ticketreservation.model.enums.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCategory(EventCategory category);

    List<Event> findByLocation(String location);

    List<Event> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
