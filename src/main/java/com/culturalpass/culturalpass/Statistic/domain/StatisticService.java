package com.culturalpass.culturalpass.Statistic.domain;

import com.culturalpass.culturalpass.Event.domain.Event;
import com.culturalpass.culturalpass.Event.domain.EventRegistrationToken;
import com.culturalpass.culturalpass.Event.infrastructure.EventRegistrationTokenRepository;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import com.culturalpass.culturalpass.Statistic.dto.*;
import com.culturalpass.culturalpass.Statistic.exceptions.InvalidDateRangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatisticService {

    @Autowired
    private EventRegistrationTokenRepository tokenRepository;

    @Autowired
    private EventRepository eventRepository;

    public MonthlyUsersEnrolledDto getMonthlyUsersEnrolled(Integer month, Integer year) {
        validateMonthYear(month, year);

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(year, month, YearMonth.of(year, month).lengthOfMonth(), 23, 59, 59);

        long totalEnrollments = tokenRepository.findAll().stream()
                .filter(token -> !token.getCreatedAt().isBefore(startOfMonth) && !token.getCreatedAt().isAfter(endOfMonth))
                .count();

        log.info("Total de inscripciones en {}/{}: {}", month, year, totalEnrollments);

        return MonthlyUsersEnrolledDto.builder()
                .totalUsers((int) totalEnrollments)
                .month(month)
                .year(year)
                .build();
    }

    public PendingEventsDto getPendingEvents() {
        LocalDateTime today = LocalDateTime.now();

        long pendingEvents = eventRepository.findAll().stream()
                .filter(event -> event.getEndDate().isAfter(today))
                .count();

        log.info("Eventos pendientes a partir de {}: {}", today, pendingEvents);

        return PendingEventsDto.builder()
                .totalPendingEvents(pendingEvents)
                .currentDate(today)
                .build();
    }

    public YearlyEventsDto getYearlyEvents(Integer year) {
        if (year == null || year < 1900 || year > 2100) {
            throw new InvalidDateRangeException("El año debe estar entre 1900 y 2100");
        }

        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        long totalEvents = eventRepository.findAll().stream()
                .filter(event ->
                        (!event.getStartDate().isBefore(startOfYear) && !event.getStartDate().isAfter(endOfYear)) ||
                                (!event.getEndDate().isBefore(startOfYear) && !event.getEndDate().isAfter(endOfYear)) ||
                                (event.getStartDate().isBefore(startOfYear) && event.getEndDate().isAfter(endOfYear))
                )
                .count();

        log.info("Eventos programados en el año {}: {}", year, totalEvents);

        return YearlyEventsDto.builder()
                .totalEvents(totalEvents)
                .year(year)
                .build();
    }

    public MonthlyRevenueDto getMonthlyRevenue(Integer month, Integer year) {
        validateMonthYear(month, year);

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(year, month, YearMonth.of(year, month).lengthOfMonth(), 23, 59, 59);

        List<Event> monthlyEvents = eventRepository.findAll().stream()
                .filter(event ->
                        (!event.getStartDate().isBefore(startOfMonth) && !event.getStartDate().isAfter(endOfMonth)) ||
                                (!event.getEndDate().isBefore(startOfMonth) && !event.getEndDate().isAfter(endOfMonth)) ||
                                (event.getStartDate().isBefore(startOfMonth) && event.getEndDate().isAfter(endOfMonth))
                )
                .toList();

        int totalEnrollments = monthlyEvents.stream()
                .mapToInt(Event::getCurrentEnrollments)
                .sum();

        double totalRevenue = monthlyEvents.stream()
                .mapToDouble(event -> event.getCurrentEnrollments() * event.getCostEntry())
                .sum();

        log.info("Recaudación mensual {}/{}: {} con {} inscripciones", month, year, totalRevenue, totalEnrollments);

        return MonthlyRevenueDto.builder()
                .totalRevenue(totalRevenue)
                .totalEnrollments(totalEnrollments)
                .month(month)
                .year(year)
                .build();
    }

    public MonthlyEnrollmentRecordDto getMonthlyEnrollmentRecord(Integer month, Integer year) {
        validateMonthYear(month, year);

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(year, month, YearMonth.of(year, month).lengthOfMonth(), 23, 59, 59);

        List<EventRegistrationToken> monthlyTokens = tokenRepository.findAll().stream()
                .filter(token -> !token.getCreatedAt().isBefore(startOfMonth) && !token.getCreatedAt().isAfter(endOfMonth))
                .collect(Collectors.toList());

        Map<LocalDateTime, Long> enrollmentsByDay = monthlyTokens.stream()
                .collect(Collectors.groupingBy(
                        token -> token.getCreatedAt().toLocalDate().atStartOfDay(),
                        Collectors.counting()
                ));

        List<DailyEnrollmentRecordDto> dailyRecords = new ArrayList<>();
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDateTime date = LocalDateTime.of(year, month, day, 0, 0);
            Long enrollments = enrollmentsByDay.getOrDefault(date, 0L);
            dailyRecords.add(DailyEnrollmentRecordDto.builder()
                    .date(date)
                    .enrollments(enrollments)
                    .build());
        }

        log.info("Record mensual {}/{}: {} inscripciones totales", month, year, monthlyTokens.size());

        return MonthlyEnrollmentRecordDto.builder()
                .month(month)
                .year(year)
                .totalEnrollments((long) monthlyTokens.size())
                .dailyRecords(dailyRecords)
                .build();
    }

    private void validateMonthYear(Integer month, Integer year) {
        if (month == null || month < 1 || month > 12) {
            throw new InvalidDateRangeException("El mes debe estar entre 1 y 12");
        }
        if (year == null || year < 1900 || year > 2100) {
            throw new InvalidDateRangeException("El año debe estar entre 1900 y 2100");
        }
    }
}