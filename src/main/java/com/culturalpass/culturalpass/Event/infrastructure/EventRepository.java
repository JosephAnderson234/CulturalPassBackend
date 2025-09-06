package com.culturalpass.culturalpass.Event.infrastructure;

import com.culturalpass.culturalpass.Event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);
}