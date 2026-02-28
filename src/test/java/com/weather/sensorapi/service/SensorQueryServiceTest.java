package com.weather.sensorapi.service;

import com.weather.sensorapi.dto.QueryRequest;
import com.weather.sensorapi.dto.QueryResult;
import com.weather.sensorapi.enums.Metric;
import com.weather.sensorapi.enums.Statistic;
import com.weather.sensorapi.exception.InvalidQueryException;
import com.weather.sensorapi.repository.SensorReadingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorQueryServiceTest {

    @Mock
    private SensorReadingRepository repository;

    @InjectMocks
    private SensorQueryService service;

    @Test
    void query_withNoDates_defaultsToLast24Hours() {
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{"sensor-1", Metric.TEMPERATURE, 22.5});
        when(repository.findAvgAllSensors(any(), any(), any())).thenReturn(rows);

        QueryRequest request = new QueryRequest(null, List.of(Metric.TEMPERATURE), Statistic.AVERAGE, null, null);
        List<QueryResult> results = service.query(request);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).value()).isEqualTo(22.5);
        verify(repository).findAvgAllSensors(any(), any(), any());
    }

    @Test
    void query_withSpecificSensors_callsSensorSpecificRepository() {
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{"sensor-1", Metric.HUMIDITY, 40.0});
        when(repository.findMinBySensors(any(), any(), any(), any())).thenReturn(rows);

        QueryRequest request = new QueryRequest(
                List.of("sensor-1"), List.of(Metric.HUMIDITY), Statistic.MIN, null, null);
        List<QueryResult> results = service.query(request);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).sensorId()).isEqualTo("sensor-1");
        assertThat(results.get(0).statistic()).isEqualTo("MIN");
        verify(repository).findMinBySensors(any(), any(), any(), any());
    }

    @Test
    void query_withFromAfterTo_throwsInvalidQueryException() {
        QueryRequest request = new QueryRequest(
                null, List.of(Metric.TEMPERATURE), Statistic.AVERAGE,
                LocalDate.now(), LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> service.query(request))
                .isInstanceOf(InvalidQueryException.class)
                .hasMessageContaining("'from' date must be before 'to' date");
    }

    @Test
    void query_withDateRangeExceeding31Days_throwsInvalidQueryException() {
        QueryRequest request = new QueryRequest(
                null, List.of(Metric.TEMPERATURE), Statistic.AVERAGE,
                LocalDate.now().minusDays(32), LocalDate.now());

        assertThatThrownBy(() -> service.query(request))
                .isInstanceOf(InvalidQueryException.class)
                .hasMessageContaining("Date range must not exceed 31 days");
    }

    @Test
    void query_withOnlyFromDate_throwsInvalidQueryException() {
        QueryRequest request = new QueryRequest(
                null, List.of(Metric.TEMPERATURE), Statistic.AVERAGE,
                LocalDate.now().minusDays(3), null);

        assertThatThrownBy(() -> service.query(request))
                .isInstanceOf(InvalidQueryException.class)
                .hasMessageContaining("Both 'from' and 'to' dates must be provided together");
    }

    @Test
    void query_withMaxStatistic_callsMaxRepository() {
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{"sensor-2", Metric.WIND_SPEED, 85.0});
        when(repository.findMaxAllSensors(any(), any(), any())).thenReturn(rows);

        QueryRequest request = new QueryRequest(
                null, List.of(Metric.WIND_SPEED), Statistic.MAX, null, null);
        List<QueryResult> results = service.query(request);

        assertThat(results.get(0).value()).isEqualTo(85.0);
        verify(repository).findMaxAllSensors(any(), any(), any());
    }
}
