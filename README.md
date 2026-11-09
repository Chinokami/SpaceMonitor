# SpaceMonitor — Astronaut Health Monitoring Backend

## Overview

SpaceMonitor is a Java-based backend application designed for an astronaut health monitoring system during space missions.

The application collects, stores, and provides access to astronaut health indicators through a REST API. It uses PostgreSQL for data persistence and Spring Boot for backend functionality.

This repository contains the Java backend component of the project. The frontend and Python backend are developed separately.

## Tech Stack

- **Java 25**
- **Spring Boot 4.1.2**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **Maven**
- **REST API**

## Features

- Management of astronaut records.
- Health indicator data retrieval.
- Measurement history storage.
- Individual astronaut health status.
- General status retrieval for all astronauts.
- Automatic measurement generation every 2 minutes.
- Support for numeric, paired, and image-based indicators.
- Local image dataset integration.

## Architecture

The application consists of several main components:

- **API Layer** — handles HTTP requests and returns JSON responses.
- **Service Layer** — processes monitoring data and application logic.
- **Repository Layer** — communicates with the PostgreSQL database.
- **Database** — stores astronauts, indicators, and measurements.
- **Scheduler** — periodically generates demonstration measurements.

The Java backend communicates with the React frontend through HTTP requests. It does not directly communicate with the separate Python backend.

## Requirements

Before running the application, make sure you have installed:

- Java 25 or compatible JDK.
- PostgreSQL.
- Maven (or use the included Maven Wrapper).
- Git (optional).

## Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/SpaceMonitor.git
cd SpaceMonitor
```

### 2. Create the database

Open PostgreSQL and create a database named:

```sql
CREATE DATABASE space_monitor;
```

### 3. Configure database connection

Open:

`src/main/resources/application.properties`

The default configuration uses:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/space_monitor}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:}
```

Set your PostgreSQL password using an environment variable.

**Windows PowerShell:**

```powershell
$env:DB_PASSWORD="YOUR_POSTGRES_PASSWORD"
```

Optional environment variables:

- `DB_URL` — PostgreSQL connection URL.
- `DB_USERNAME` — PostgreSQL username.
- `DB_PASSWORD` — PostgreSQL password.
- `CORS_ALLOWED_ORIGINS` — allowed frontend origins.
- `IMAGE_DATASET_DIRECTORY` — local image dataset directory.

### 4. Run the application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

The backend runs at:

```text
http://localhost:8080
```

Hibernate automatically manages the database schema using the configured JPA settings.

API Documentation

Base URL:

http://localhost:8080

All endpoints below use the HTTP GET method and return JSON responses.

Method

Endpoint

Description

GET

/api/astronauts

Retrieves all astronauts.

GET

/api/status

Retrieves the monitoring status of all astronauts.

GET

/api/astronauts/{astronautId}/status

Retrieves all available indicators for a specific astronaut.

GET

/api/astronauts/{astronautId}/indicators/{indicatorId}

Retrieves a specific indicator and its measurement history.

Example Requests

Retrieve all astronauts:

GET http://localhost:8080/api/astronauts

Retrieve the general status:

GET http://localhost:8080/api/status

Retrieve an astronaut's health status:

GET http://localhost:8080/api/astronauts/1/status

Retrieve a specific indicator:

GET http://localhost:8080/api/astronauts/1/indicators/1

Replace the IDs with actual IDs returned by the API.

Frontend Integration

The React frontend should communicate with this backend using HTTP requests.

Example using JavaScript Fetch API:

const response = await fetch(
  "http://localhost:8080/api/astronauts/1/status"
);

const data = await response.json();

console.log(data);

The exact response structure should be determined from the actual API responses. Use Postman to inspect the returned JSON when implementing frontend components.

Database

The application uses PostgreSQL to store:

Astronauts — astronaut records.

Indicators — available health indicators and their configuration.

Measurements — individual readings, timestamps, and associated astronauts.

Each measurement is associated with an astronaut and an indicator.

Measurement history is stored in the database, allowing the frontend to retrieve previous readings.

Automatic Data Updates

The application automatically generates demonstration measurements:

An initial measurement generation is performed after application startup.

New measurements are generated every 2 minutes.

Measurements are stored in PostgreSQL.

Each astronaut has separate measurement records.

The generated values are intended for demonstration and testing purposes only. They are not real medical measurements or medically validated astronaut health thresholds.

Image Dataset

The application supports local image dataset integration.

Place image files in the configured dataset directory:

./data/images

Supported image files are selected from the available local dataset.

Images are exposed through the /media/ URL path.

The application does not generate artificial medical images when the dataset is unavailable.

Important Notes

This repository contains only the Java Spring Boot backend.

The Python backend and React frontend are separate project components.

PostgreSQL must be running before starting the application.

The backend uses demonstration data for automatic measurement generation.

The application is a hackathon prototype and is not intended for real medical diagnosis or clinical use.

License

This project was developed as part of a hackathon.
