package com.weather.sensorapi.controller;

import com.weather.sensorapi.dto.QueryResult;
import com.weather.sensorapi.enums.Metric;
import com.weather.sensorapi.enums.Statistic;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorQueryController {

    @GetMapping("/query")
    public List<QueryResult> query(
            @RequestParam(required = false) List<String> sensorIds,
            @RequestParam List<Metric> metrics,
            @RequestParam Statistic statistic,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        // TODO: implement query logic
        return List.of();
    }
}
