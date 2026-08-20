# Badges App

Badges App is a Spring Boot REST API for managing employee badges and airport access. It was developed as an internship project and demonstrates a layered backend architecture, role-based access control with Keycloak, persistence with PostgreSQL, DTO mapping with MapStruct, and automated tests.

## What it does

- Manages users, companies, countries, cities, and airports
- Issues badges and assigns time-bounded airport access
- Tracks leave requests, notifications, and badge status
- Synchronizes Keycloak users with the application database
- Secures API endpoints with JWT authentication and role-based authorization

## Tech stack

- Java 17 and Spring Boot 3
- Spring Web, Spring Data JPA, and Spring Security
- PostgreSQL
- Keycloak OAuth 2.0 / OpenID Connect
- MapStruct and Lombok
- JUnit 5, Mockito, MockMvc, JaCoCo, and SonarQube
- Docker and Maven Wrapper

## Architecture

The code follows a conventional layered structure:

```text
controllers -> services -> repositories -> PostgreSQL
      |            |
      +---------- mappers ----------> DTOs
                   |
                Keycloak
```

Application code lives under `src/main/java/com/ram/badgesapp`, grouped by controller, service, repository, entity, DTO, mapper, and configuration concerns.

## Prerequisites

- JDK 17 or newer
- PostgreSQL
- Keycloak with a realm, roles, and an admin service client configured

## Configuration

Copy `.env.example` and provide values through your IDE, shell, container runtime, or deployment platform. The application intentionally does not load `.env` files by itself.

The most important variables are:

| Variable | Purpose |
| --- | --- |
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | PostgreSQL connection |
| `KEYCLOAK_ISSUER_URI` | JWT issuer used by the resource server |
| `KEYCLOAK_ADMIN_CLIENT_ID`, `KEYCLOAK_ADMIN_CLIENT_SECRET` | Keycloak admin service client |
| `KEYCLOAK_SYNC_ON_STARTUP` | Enables the optional admin-user sync at startup |
| `CORS_ALLOWED_ORIGINS` | Comma-separated trusted frontend origins |

Defaults for local service URLs are defined in `application.properties`; secrets have no committed default.

## Run locally

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080` by default. Protected endpoints require a bearer token issued by the configured Keycloak realm.

## Tests

The test suite uses an in-memory H2 database in PostgreSQL compatibility mode, so PostgreSQL and Keycloak do not need to be running:

```powershell
.\mvnw.cmd test
```

JaCoCo writes the coverage report to `target/site/jacoco/index.html`.

## Docker

Build the application image from the repository root:

```bash
docker build -t badges-app .
```

Pass the configuration variables at runtime and ensure the container can reach PostgreSQL and Keycloak.

## Project history

See [CHANGES.md](CHANGES.md) for the Keycloak synchronization feature notes.

## Security note

Never commit real database passwords, client secrets, tokens, or private environment files. If a secret has ever been committed, rotate it at the provider; removing it from the latest revision does not remove it from Git history.
