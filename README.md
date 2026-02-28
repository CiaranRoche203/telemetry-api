# sensor-api

## Setup

Requires Java 17, Maven and PostgreSQL.

Create a database called `sensordb` and update the credentials in `application.properties` if needed, then run:

```bash
mvn spring-boot:run
```

App runs on port 8080.

## Tests

```bash
mvn test
```

Tests use H2 in-memory so no database needed.

## Endpoints

`POST /api/sensors/{sensorId}/readings` - record a reading for a sensor

`GET /api/sensors/query` - query readings, supports filtering by sensor, metric, statistic and date range

Metrics: `TEMPERATURE`, `HUMIDITY`, `WIND_SPEED`, `PRESSURE`, `UV_INDEX`

Statistics: `MIN`, `MAX`, `SUM`, `AVERAGE`

### Example of Queries

GET - Query last 24 hours (no dates)
GET http://localhost:8080/api/sensors/query?metrics=TEMPERATURE&statistic=AVERAGE

GET - Query specific sensor with date range
GET http://localhost:8080/api/sensors/query?sensorIds=sensor-1&metrics=TEMPERATURE&statistic=AVERAGE&from=2026-02-01&to=2026-02-28

GET - Multiple metrics
GET http://localhost:8080/api/sensors/query?metrics=TEMPERATURE&metrics=HUMIDITY&statistic=MIN

GET - All sensors, MAX statistic
GET http://localhost:8080/api/sensors/query?metrics=WIND_SPEED&statistic=MAX
