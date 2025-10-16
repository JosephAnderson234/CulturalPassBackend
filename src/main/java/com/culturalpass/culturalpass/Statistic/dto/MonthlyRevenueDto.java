package com.culturalpass.culturalpass.Statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyRevenueDto {
    private Double totalRevenue;
    private Integer totalEnrollments;
    private Integer month;
    private Integer year;
}