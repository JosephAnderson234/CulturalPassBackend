package com.culturalpass.culturalpass.Statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingEventsDto {
    private Long totalPendingEvents;
    private LocalDate currentDate;
}
