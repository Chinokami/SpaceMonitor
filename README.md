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

## API Documentation

Base URL:

```text
