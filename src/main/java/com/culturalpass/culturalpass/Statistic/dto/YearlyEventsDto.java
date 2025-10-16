package com.culturalpass.culturalpass.Statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearlyEventsDto {
    private Long totalEvents;
    private Integer year;
}