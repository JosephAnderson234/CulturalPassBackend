package com.culturalpass.culturalpass.Event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.OffsetDateTime;

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
    private OffsetDateTime  eventDate;
    private boolean validated;
    private OffsetDateTime  createdAt;
    private OffsetDateTime validatedAt;
}
