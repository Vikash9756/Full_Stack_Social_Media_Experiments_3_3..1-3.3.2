# Social Media Management System — Experiments 3.3.1 + Capstone

**Course experiments:** Full-stack integration and production-style Social Media Management System  
**Stack:** React + Vite, Spring Boot 3 / Java 17, Spring Security JWT, PostgreSQL, Kafka, Docker Compose, GitHub Actions, Swagger/OpenAPI.

## Included workflows
- JWT login/register and role-based access scaffolding (`ADMIN`, `EDITOR`, `VIEWER`)
- Create/list posts
- Schedule a post with date/time
- Calendar-friendly post list
- Basic analytics summary
- Kafka event producer/consumer scaffold for scheduled publishing
- OpenAPI Swagger UI
- Docker Compose for app dependencies
- CI workflow for frontend build and backend tests

> Educational starter project, not a security-audited production system. Before real deployment, add secret management, refresh-token/revocation strategy, migrations, rate limiting, observability, robust scheduling/locking, platform-specific publishing integrations, and expand tests to meet the course's ≥80% coverage requirement. Kafka event flow is demonstrated; publishing to real social media platforms requires their official APIs and credentials.

## Requirements
- JDK 17+
- Maven 3.9+
- Node.js 20+
- Docker Desktop / Docker Engine
- Git and GitHub account

## Run with Docker Compose
From this folder:
```bash
docker compose up --build
```
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Frontend: http://localhost:5173
- PostgreSQL: localhost:5432
- Kafka: localhost:9092

If running frontend/backend separately, see their READMEs.

## Run tests/build manually
Backend:
```bash
cd backend
mvn clean verify
```
Frontend:
```bash
cd frontend
npm install
npm run build
```

## Quick demo
1. Open http://localhost:5173.
2. Register a user from the login panel (first user is not automatically admin; roles are assigned by the backend registration policy).
3. Log in and use the Posts section to create or schedule posts.
4. See post counts in Analytics.
5. Swagger UI documents available endpoints.

## Experiment 3.3.1 mapping
1. React frontend calls Spring Boot REST APIs.
2. JWT login stores token in browser session storage for this demo.
3. Post composer sends post content to backend.
4. Scheduled posts store `scheduledAt` and status.
5. Calendar view lists scheduled posts by date.
6. Analytics endpoint returns counts by status.
7. UI shows loading and error states.

## Capstone mapping
- Multi-user roles: role field and Spring Security authorities (extend admin/user management for a full team workflow).
- Scheduling + Kafka: scheduled post creation publishes an event; consumer logs the event. For production, use a durable scheduler/outbox and only publish when due.
- API documentation: Swagger UI.
- CI/CD: GitHub Actions workflow builds/tests frontend and backend.
- Tests: backend service/controller test examples and frontend build. This starter does **not** claim ≥80% coverage; measure with JaCoCo and add tests before submission/deployment.

## Notes
- Change `JWT_SECRET` in the environment before deployment to a long random secret.
- Demo JWT implementation is intentionally simple; do not expose it to the public internet without security review.
