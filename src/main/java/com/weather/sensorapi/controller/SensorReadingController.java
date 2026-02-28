package com.weather.sensorapi.controller;

import com.weather.sensorapi.dto.SensorReadingRequest;
import com.weather.sensorapi.model.SensorReading;
import com.weather.sensorapi.service.SensorReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorReadingController {

    private final SensorReadingService service;

    @PostMapping("/{sensorId}/readings")
    @ResponseStatus(HttpStatus.CREATED)
    public SensorReading record(@PathVariable String sensorId,
                                @Valid @RequestBody SensorReadingRequest request) {
        return service.record(sensorId, request);
    }
}
