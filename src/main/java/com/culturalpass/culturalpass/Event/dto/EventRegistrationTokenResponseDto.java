package com.culturalpass.culturalpass.Event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationTokenResponseDto {
    private Long id;
    private String token;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long eventId;
    private String eventTitle;
    private LocalDate eventDate;
    private boolean validated;
    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;
}
