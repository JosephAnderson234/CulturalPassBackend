package com.culturalpass.culturalpass.Event.dto;

import com.culturalpass.culturalpass.Event.domain.EventType;
import com.culturalpass.culturalpass.Event.domain.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime ;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private String title;
    private String description;
    private String imageUrl; // Opcional
    private OffsetDateTime  startDate;
    private OffsetDateTime  endDate;
    private String location;
    private EventType type;
    private EventStatus status;
    private List<String> tags;
}