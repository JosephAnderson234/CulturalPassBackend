package com.culturalpass.culturalpass.Event.domain;

import com.culturalpass.culturalpass.User.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String title;

    @NotBlank
    private String description;

    @URL
    private String imageUrl = null;

    @NotNull
    private OffsetDateTime  startDate;

    @NotNull
    private OffsetDateTime endDate;

    @NotNull
    private String location;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EventType type;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EventStatus status;

    @Column(nullable = false)
    private Integer capacity = 0;

    @Column(nullable = false)
    private Double costEntry = 0.0;

    @Column(nullable = false)
    private Integer currentEnrollments = 0;

    @ElementCollection
    @CollectionTable(
            name = "event_tags",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "tag")
    private List<String> tags;

    @ManyToMany
    @JoinTable(
            name = "event_registrations",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> registeredUsers;
}