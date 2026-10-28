# Backend
Spring Boot REST API with JWT authentication, PostgreSQL persistence, Kafka event demo, and Swagger/OpenAPI.

```bash
mvn clean verify
mvn spring-boot:run
```
Swagger: `http://localhost:8080/swagger-ui/index.html`

Register/login: `POST /api/auth/register`, `POST /api/auth/login` with `{"email":"student@example.com","password":"password123"}`. Pass the returned token as `Authorization: Bearer <token>` to `/api/posts` and `/api/analytics/summary`.

Kafka currently consumes a scheduled-post event and logs it. It does not publish to an external social network and does not itself guarantee execution at the requested scheduled time.
