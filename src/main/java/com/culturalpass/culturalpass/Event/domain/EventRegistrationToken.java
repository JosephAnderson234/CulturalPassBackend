package com.culturalpass.culturalpass.Event.domain;

import com.culturalpass.culturalpass.User.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "event_registration_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private boolean validated = false;

    @Column(nullable = false)
    private OffsetDateTime  createdAt = OffsetDateTime.now();

    @Column
    private OffsetDateTime  validatedAt;

    public EventRegistrationToken(String token, User user, Event event) {
        this.token = token;
        this.user = user;
        this.event = event;
    }
}