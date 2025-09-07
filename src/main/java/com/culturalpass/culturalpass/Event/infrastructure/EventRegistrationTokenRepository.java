package com.culturalpass.culturalpass.Event.infrastructure;

import com.culturalpass.culturalpass.Event.domain.Event;
import com.culturalpass.culturalpass.Event.domain.EventRegistrationToken;
import com.culturalpass.culturalpass.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationTokenRepository extends JpaRepository<EventRegistrationToken, Long> {
    Optional<EventRegistrationToken> findByToken(String token);
    Optional<EventRegistrationToken> findByUserAndEvent(User user, Event event);
    List<EventRegistrationToken> findByUser(User user);
    List<EventRegistrationToken> findByEvent(Event event);
    List<EventRegistrationToken> findByValidated(boolean validated);
}