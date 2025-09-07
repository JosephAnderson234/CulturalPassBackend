package com.culturalpass.culturalpass.Event.domain;

import com.culturalpass.culturalpass.User.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
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
    private String imageUrl;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private String location;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EventType type;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EventStatus status;

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
