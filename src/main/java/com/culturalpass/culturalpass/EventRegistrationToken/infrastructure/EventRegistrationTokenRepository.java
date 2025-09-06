package com.culturalpass.culturalpass.EventRegistrationToken.infrastructure;

import com.culturalpass.culturalpass.Event.domain.Event;
import com.culturalpass.culturalpass.EventRegistrationToken.domain.EventRegistrationToken;
import com.culturalpass.culturalpass.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRegistrationTokenRepository extends JpaRepository<EventRegistrationToken, Long> {
    Optional<EventRegistrationToken> findByToken(String token);
    boolean existsByUserAndEvent(User user, Event event);
}