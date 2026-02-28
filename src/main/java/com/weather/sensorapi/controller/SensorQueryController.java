package com.weather.sensorapi.controller;

import com.weather.sensorapi.dto.QueryRequest;
import com.weather.sensorapi.dto.QueryResult;
import com.weather.sensorapi.enums.Metric;
import com.weather.sensorapi.enums.Statistic;
import com.weather.sensorapi.service.SensorQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorQueryController {

    private final SensorQueryService service;

    @GetMapping("/query")
    public List<QueryResult> query(
            @RequestParam(required = false) List<String> sensorIds,
            @RequestParam List<Metric> metrics,
            @RequestParam Statistic statistic,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.query(new QueryRequest(sensorIds, metrics, statistic, from, to));
    }
}
