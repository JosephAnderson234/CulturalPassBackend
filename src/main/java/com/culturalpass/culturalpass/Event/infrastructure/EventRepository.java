package com.culturalpass.culturalpass.Event.infrastructure;

import com.culturalpass.culturalpass.Event.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    boolean existsByTitle(String title);
    Page<Event> findByRegisteredUsersId(Long userId, Pageable pageable);
}