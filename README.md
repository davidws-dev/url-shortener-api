# URL Shortener API

A production-ready REST API for URL shortening built with Spring Boot, PostgreSQL and Docker. Includes a live demo page, Swagger documentation and full deployment on Railway.

🔗 **Live Demo:** [url-shortener-api-production-d27f.up.railway.app](https://url-shortener-api-production-d27f.up.railway.app)  
📄 **Swagger UI:** [/swagger-ui/index.html](https://url-shortener-api-production-d27f.up.railway.app/swagger-ui/index.html)

---

## Features

- **Shorten URLs** — generates a unique 6-character alphanumeric code
- **Redirect** — `GET /{code}` redirects to the original URL with HTTP 302
- **Access tracking** — counts every redirect hit
- **Optional expiration** — URLs can be set to expire at a specific date/time
- **Info endpoint** — retrieve metadata without incrementing the counter
- **Swagger UI** — auto-generated API documentation via SpringDoc OpenAPI
- **Live demo page** — static frontend served directly by Spring Boot

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4.4 |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA / Hibernate |
| Validation | Spring Validation (Jakarta) |
| Documentation | SpringDoc OpenAPI 2.8.8 |
| Containerization | Docker / Docker Compose |
| Deploy | Railway |
| Build | Maven Wrapper |

---

## Project Structure

```
src/main/java/com/davidws/urlshortener/
├── UrlshortenerApplication.java
├── controller/
│   ├── RedirectController.java        # GET /{code} → redirect
│   └── UrlShortenerController.java    # POST /shorten, GET /{code}/info
├── dto/
│   ├── ShortenRequest.java
│   ├── ShortenResponse.java
│   └── UrlInfoResponse.java
├── entity/
│   └── UrlEntity.java
├── exception/
│   ├── GlobalExceptionHandler.java    # @RestControllerAdvice
│   └── UrlNotFoundException.java
├── repository/
│   └── UrlRepository.java
└── service/
    └── UrlShortenerService.java

src/main/resources/
├── static/
│   └── index.html                     # Live demo page
└── application.yml                    # Environment-based config
```

---

## API Endpoints

### `POST /api/v1/urls/shorten`
Shortens a URL and returns the short code and full short URL.

**Request body:**
```json
{
  "originalUrl": "https://example.com/very/long/url",
  "expiresAt": "2026-12-31T23:59:59"
}
```

**Response `201 Created`:**
```json
{
  "shortCode": "AbC123",
  "shortUrl": "https://your-domain.com/AbC123"
}
```

---

### `GET /{shortCode}`
Redirects to the original URL.

**Response `302 Found`** with `Location` header pointing to the original URL.  
Returns `404` if the code doesn't exist or has expired.

---

### `GET /api/v1/urls/{shortCode}/info`
Returns metadata about a shortened URL without incrementing the access counter.

**Response `200 OK`:**
```json
{
  "originalUrl": "https://example.com/very/long/url",
  "shortCode": "AbC123",
  "createdAt": "2026-05-17T20:00:00",
  "expiresAt": null,
  "accessCount": 42
}
```

---

## Error Responses

All errors return a consistent JSON format:

```json
{
  "timestamp": "2026-05-17T20:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "URL não encontrada para o código: AbC123"
}
```

---

## Running Locally

### Prerequisites
- Java 17+
- Docker Desktop

### 1. Clone the repository
```bash
git clone https://github.com/davidws-dev/url-shortener-api.git
cd url-shortener-api
```

### 2. Start PostgreSQL with Docker
```bash
docker-compose up -d
```

### 3. Run the application with local profile
```bash
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

### 4. Access
| Resource | URL |
|---|---|
| Demo page | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| API base | http://localhost:8080/api/v1/urls |

---

## Environment Variables

The application uses environment variables for all sensitive configuration:

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_URL` | Full JDBC connection URL |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `APP_BASE_URL` | Base URL used to generate short links |
| `PORT` | Server port (default: 8080) |

For local development, create `src/main/resources/application-local.yml` (excluded from git):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5433/urlshortener
    username: urlshortener_user
    password: postgres

app:
  base-url: http://localhost:8080
```

---

## Deployment

This project is deployed on **Railway** with a managed PostgreSQL instance.

Every push to `main` triggers an automatic redeploy via GitHub integration.

Build is handled by **Railpack** using Java 17 and Maven 3.9.9.

---

## Challenges & Solutions

| Challenge | Solution |
|---|---|
| PostgreSQL port conflict on Windows | Changed Docker container port to `5433` |
| `application.yml` corrupted with docker-compose content | Replaced with clean Spring-only config |
| Invalid Spring Boot version (`3.5.14`) | Downgraded to stable `3.4.4` |
| Java 21 requested, Java 17 available | Set `<java.version>17</java.version>` in `pom.xml` |
| Swagger mapping error with springdoc `2.8.16` | Pinned to compatible version `2.8.8` |
| Port 8080 occupied by previous process | Identified PID with `netstat`, killed with `taskkill` |
| Missing table permissions for `urlshortener_user` | Granted privileges on table and sequence |
| Hibernate not creating table automatically | Created table manually via SQL |
| Railway `DATABASE_URL` missing `jdbc:` prefix | Switched to `PGHOST`/`PGPORT`/`PGDATABASE` variables |
| Maven 3.9.16 not found on Railway (404) | Pinned to `3.9.9` via `railpack.json` |
| Static `localhost:8080` in short URL generation | Injected `APP_BASE_URL` via `@Value` |

---

## Roadmap

- [ ] Input validation — verify URL format before saving
- [ ] Global exception handler improvements
- [ ] Cache with `@Cacheable` for frequently accessed URLs
- [ ] Rate limiting by IP
- [ ] Unit and integration tests
- [ ] Custom short codes chosen by the user
- [ ] Structured logging with SLF4J
- [ ] Flyway for database migrations

---

## Author

**David Silva** — Junior Backend Developer  
📍 Porto, Portugal  
🌐 [davidws.pt](https://www.davidws.pt)  
💼 [linkedin.com/in/davidws-dev](https://linkedin.com/in/davidws-dev)  
🐙 [github.com/davidws-dev](https://github.com/davidws-dev)