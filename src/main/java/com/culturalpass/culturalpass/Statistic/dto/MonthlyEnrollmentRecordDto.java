package com.culturalpass.culturalpass.Statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyEnrollmentRecordDto {
    private Integer month;
    private Integer year;
    private Long totalEnrollments;
    private List<DailyEnrollmentRecordDto> dailyRecords;
}