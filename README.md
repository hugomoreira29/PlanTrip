# PlanTrip

Full-stack trip planner: a REST API built with Spring Boot and a React frontend.

## Features

- Trips with a day-by-day itinerary and places to visit
- Automatic coordinates for places using OpenStreetMap (Nominatim), with caching and rate limiting
- Trip budget: expenses by category in any currency
- Budget summary converted to a chosen currency (Frankfurter exchange rates, cached)

## Stack

- Backend: Java 21, Spring Boot, Spring Security (JWT), JPA, Flyway, PostgreSQL
- Frontend: React, TypeScript, Vite (in development)
- Infrastructure: Docker, GitHub Actions

## Prerequisites

- Java 21
- Docker

## Getting started (development)

1. Start the database (from the repository root):
   ```bash
   docker compose up -d
   ```

2. Start the backend:
   ```bash
   cd plantrip-api
   ./mvnw spring-boot:run
   ```

3. Open the API documentation (Swagger UI): http://localhost:8080/swagger-ui.html

## Running tests

With the database running:

```bash
cd plantrip-api
./mvnw test
```

## Project status

- [x] Phase 0: Setup
- [x] Phase 1: Trips and itinerary
- [x] Phase 2: Map
- [x] Phase 3: Budget
- [ ] Phase 4: Users and security
- [ ] Phase 5: Technical extras
- [ ] Phase 6: Quality
- [ ] React frontend