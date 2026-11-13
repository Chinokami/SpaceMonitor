# SpaceMonitor — Java Health Monitoring Backend

Java Spring Boot backend for the SpaceMonitor astronaut health monitoring application.

The mobile frontend stores the local user profile (name, surname, height, weight, day streak) and local activity logs on the device. This backend stores monitoring indicators, measurement history, and configurable status ranges in PostgreSQL. The separate Python backend is not called by this service.

## Tech stack

- Java 25
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven Wrapper

## Responsibilities

- Provide indicator metadata and latest measurements.
- Provide measurement history.
- Calculate an indicator status from ranges stored in `graded_values`.
- Generate demonstration measurements every two minutes.
- Serve local image dataset files under `/media/`.

The `astronauts` row is retained as an internal technical owner because existing measurements have a foreign key to it. It is not the user's profile and is not exposed through the API. The initializer keeps exactly one such row and reassigns existing measurements to it if duplicate astronaut rows are found.

## Requirements

- JDK 25
- PostgreSQL
- Git (optional)

## Setup

### 1. Clone

```bash
git clone https://github.com/Chinokami/SpaceMonitor.git
cd SpaceMonitor
```

### 2. Create the database

```sql
CREATE DATABASE space_monitor;
```

### 3. Configure PostgreSQL

The default connection is configured in `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/space_monitor}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
```

Set the password in PowerShell before starting the application:

```powershell
$env:DB_PASSWORD="YOUR_POSTGRES_PASSWORD"
```

Optional environment variables: `DB_URL`, `DB_USERNAME`, `CORS_ALLOWED_ORIGINS`, `IMAGE_DATASET_DIRECTORY`.

### 4. Start

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080`.

Hibernate creates/updates entity tables, including `graded_values`. It does not automatically remove old tables from an existing database. If upgrading the previous version, the old `logs` table may remain in PostgreSQL, but this version no longer reads or writes it.

## API

Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/indicators` | Returns all indicators, latest measurements, history, and calculated statuses. |
| GET | `/api/indicators/{indicatorId}` | Returns one indicator, its latest measurement, history, and status. |
| GET | `/api/indicators/{indicatorId}/status` | Same response; explicit endpoint for frontend status requests. |

Example:

```http
GET http://localhost:8080/api/indicators/1/status
```

The response contains `indicatorId`, `code`, `name`, `unit`, `description`, `dataType`, `status`, `latestMeasurement`, and `history`.

`status` is one of:

- `BAD`
- `NORMAL`
- `GOOD`
- `UNKNOWN` — no measurement exists, the indicator is image-based, or no configured interval matches the latest value.

## `graded_values` table

One row represents one value interval for one indicator and one status:

| Column | Meaning |
|---|---|
| `id` | Primary key |
| `indicator_id` | Related indicator |
| `status` | `BAD`, `NORMAL`, or `GOOD` |
| `min_value`, `max_value` | Inclusive lower and upper bounds for the primary value |
| `min_secondary_value`, `max_secondary_value` | Optional bounds for a paired value, such as diastolic blood pressure |

Multiple intervals for the same indicator/status are allowed. This is needed when both unusually low and unusually high values should be classified as `BAD`.

The application intentionally does not invent medical thresholds. Until the team configures intervals, the API returns `UNKNOWN` for numeric indicators. Use `database/graded_values_template.sql` as a starting point after agreeing on the correct ranges. These values are application configuration, not medically validated guidance.

## Database tables

- `astronauts` — one internal technical record used as the owner of measurements; not the user's local profile.
- `indicators` — indicator definitions and simulation settings.
- `measurements` — readings and timestamps.
- `graded_values` — configurable status intervals.

The mobile frontend stores profile information and activity logs locally. There are no profile, streak, or log endpoints in this Java backend.

## Demonstration data

An initial set of measurements is generated after startup. Additional demonstration measurements are generated every two minutes and saved in PostgreSQL. The values are simulated and must not be treated as real astronaut measurements or used for diagnosis or treatment.

Images are read from `./data/images` by default and served under `/media/`. Set `IMAGE_DATASET_DIRECTORY` to use another directory.

## CORS

Allowed origins are configured through `CORS_ALLOWED_ORIGINS`. The default values are `http://localhost:5173` and `http://localhost:3000`.
