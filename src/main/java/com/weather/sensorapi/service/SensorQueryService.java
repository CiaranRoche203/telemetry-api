package com.weather.sensorapi.service;

import com.weather.sensorapi.dto.QueryRequest;
import com.weather.sensorapi.dto.QueryResult;
import com.weather.sensorapi.enums.Metric;
import com.weather.sensorapi.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorQueryService {

    private final SensorReadingRepository repository;

    public List<QueryResult> query(QueryRequest request) {
        LocalDateTime from;
        LocalDateTime to;

        if (request.from() == null && request.to() == null) {
            to = LocalDateTime.now();
            from = to.minusDays(1);
        } else if (request.from() == null || request.to() == null) {
            throw new IllegalArgumentException("Both 'from' and 'to' dates must be provided together");
        } else {
            if (request.from().isAfter(request.to())) {
                throw new IllegalArgumentException("'from' date must be before 'to' date");
            }
            long daysBetween = ChronoUnit.DAYS.between(request.from(), request.to());
            if (daysBetween > 31) {
                throw new IllegalArgumentException("Date range must not exceed 31 days");
            }
            from = request.from().atStartOfDay();
            to = request.to().plusDays(1).atStartOfDay().minusNanos(1);
        }

        List<Metric> metrics = request.metrics();
        List<String> sensorIds = request.sensorIds();
        boolean allSensors = sensorIds == null || sensorIds.isEmpty();

        List<Object[]> rows = switch (request.statistic()) {
            case AVERAGE -> allSensors
                    ? repository.findAvgAllSensors(metrics, from, to)
                    : repository.findAvgBySensors(sensorIds, metrics, from, to);
            case MIN -> allSensors
                    ? repository.findMinAllSensors(metrics, from, to)
                    : repository.findMinBySensors(sensorIds, metrics, from, to);
            case MAX -> allSensors
                    ? repository.findMaxAllSensors(metrics, from, to)
                    : repository.findMaxBySensors(sensorIds, metrics, from, to);
            case SUM -> allSensors
                    ? repository.findSumAllSensors(metrics, from, to)
                    : repository.findSumBySensors(sensorIds, metrics, from, to);
        };

        return rows.stream()
                .map(row -> new QueryResult(
                        (String) row[0],
                        row[1].toString(),
                        request.statistic().name(),
                        ((Number) row[2]).doubleValue()
                ))
                .toList();
    }
}
