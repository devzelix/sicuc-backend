## [1.1.0] - 2025-05-23

### ✨ Added

- The `createdAt` field was added to the `Cultist` entity. This field is automatically set when persisting to the database.

## [1.1.1] - 2025-05-23

### 📄 Documentation

- Improved README formatting
- Updated CHANGELOG
- Cleaned up `.gitignore`

## [2.0.0] - 2025-05-23

### 🚨 Breaking Changes

- Renamed several packages, public classes and fields in the API, which may break backwards compatibility.
- Examples:
  - `Cultist` → `Cultor`
  - Field `cultistsService` in `CultorController` renamed to `cultorService`
- Please review these changes carefully and update your code accordingly.

## [2.0.1] - 2025-05-27

### 🔧 Changed

- Restructured `src/test/java` to match updated package structure.
- Fixed test configuration to enable proper execution of `@SpringBootTest`.

## [3.0.0] - 2025-06-06

### 🔧 Changed

- Renamed base package structure for better organization.
- Renamed the main application class to reflect new naming convention.
- Changed `artifactId`, `groupId`, and `version` to align with new project scope.

### ✨ Added

- `server.servlet.context-path` added to `application.properties` for base path configuration.

### 🐛 Fixed

- Made `email` field optional (`nullable`) while preserving validation for non-empty values.

### 📣 Migration Note

- This version includes **breaking changes** (package names, artifactId, and context path now loaded from `.env`). Make sure to update your imports, API clients, and environment configuration accordingly.

## [3.1.0] - 2025-06-06

### 🔧 Changed

- Updated `groupId` in `pom.xml` from `com.culturacarabobo` to `com.culturacarabobo.sicuc` to follow domain-based naming conventions.
- Renamed `artifactId` from `SICUC-backend` to `backend` for consistency with modular naming.
- Set `name` in `pom.xml` to `sicuc-backend` (kebab-case) to align with Spring Boot conventions.
- Updated `description` in `pom.xml` to: _"REST service to manage carabobo cultors"_.
- Modified `spring.application.name` in `application.properties` from `carabobo-cultors` to `sicuc-backend`.

### 🗒️ Notes

- These changes are structural and do not affect runtime behavior.

## [4.0.0] - 2025-06-12

### ✨ Added

- **Support for selecting a custom artistic discipline ("Other")**
  - A new optional field was added to the `Cultor` entity:
    ```java
    private String otherDiscipline;
    ```
  - This field allows users to manually specify their artistic discipline when selecting `"Otra..."` from the predefined `artDiscipline` list.

### 🧪 Validation

- New validation logic was implemented:
  - If the selected `artDiscipline.name` is `"Otra..."`, the `otherDiscipline` field becomes **required**.
  - If not provided in this case, the backend will return a validation error.

### 🚨 Breaking Changes

- The validation logic now enforces a requirement for the `otherDiscipline` field when `"Otra..."` is selected.
- This may break existing forms or clients not updated to support this behavior.
- Be sure to update frontend validations or request payloads accordingly.

## [4.0.1] - 2025-06-13

### ✨ Added

- Relevant comments to clarify the validation logic for the `otherDiscipline` field.

## [5.0.0] - 2025-10-16

### ✨ Added

- **Implemented a full JWT authentication flow**, including:
  - Generation of short-lived `accessToken` and long-lived `refreshToken`.
  - New public endpoints under `/auth`:
    - `POST /login`: To exchange user credentials for tokens.
    - `POST /refresh`: To renew an expired `accessToken` using a `refreshToken`.
- **Foundation for Role-Based Access Control (RBAC)** by introducing `ROLE_ADMIN`, `ROLE_EDITOR`, and `ROLE_ARTIST` roles.
- **Strict token type validation**: Added `type` claims (`'access'` and `'refresh'`) to ensure each token can only be used for its intended purpose.

### 🔧 Changed

- **Completely replaced the `Basic Auth` authentication system**.
- **Reconfigured endpoint security**:
  - Form data endpoints (e.g., `GET /municipalities`, `/parishes`, `/art-categories`) are now public.
  - The endpoint for registering a new cultor (`POST /cultors`) is now public.
  - All other endpoints (e.g., `GET /cultors`) now require a valid JWT `accessToken`.
- The `User` entity was updated to implement Spring Security's `UserDetails` and include the `role` field.

### 🚨 Breaking Changes

- **`Basic Auth` support has been completely removed.** Requests using this authentication method will be rejected.
- All protected endpoints now require a **`Bearer Token`** in the `Authorization` header.
- The authentication flow has changed. Clients **must** now call `POST /auth/login` to obtain a token before accessing protected routes.

### 📣 Migration Note

- **API clients must be updated** to handle the new JWT authentication flow:
  1. Make a `POST` request to `/api/v1/auth/login` with `username` and `password`.
  2. Securely store the received `accessToken` and `refreshToken`.
  3. For all requests to protected endpoints, include the `accessToken` in the `Authorization: Bearer <token>` header.
  4. Implement logic to use the `refreshToken` at the `/api/v1/auth/refresh` endpoint when the `accessToken` expires.

---

## [5.1.0] - 2025-10-20

### ✨ Added

- **Full Dockerization of the Application and Database.**
  - Creation of a **`Dockerfile`** using a **Multi-Stage Build** (Java 21/Maven) to generate minimal and secure production images.
  - Implementation of **`docker-compose.prod.yml`** for stable deployment in production environments (backend and MySQL).
  - Implementation of **`docker-compose.dev.yml`** for a fast local development environment with volumes and improved DX.
  - Configuration of the **`.env`** file to centralize all environment variables (DB credentials, JWT keys, etc.).
  - Configured service **healthchecks** and `depends_on` to ensure the backend container waits for the database to be ready.
- **VS Code Dev Containers Support** for a unified development environment.

### 🔧 Changed

- The build process was updated to produce a slim, container-ready image.
- Database DDL configuration was separated between environments (`dev` vs. `prod`).

### 📣 Migration Note

- The primary execution method shifts from running a local JAR to **`docker compose -f docker-compose.prod.yml up -d`**.
- The next step for production safety is the implementation of **Flyway** for secure database schema management (`DDL_AUTO=none`).

## [6.0.0] - 2025-11-09

### 🚀 Major Release: Architecture Hardening & Quality Assurance

This release focuses on significantly improving the system's stability and maintainability. It introduces a full automated testing suite, standardized database schema management via Flyway, and comprehensive API documentation to ensure long-term code health.

### ✨ Added

- **🛡️ Comprehensive Automated Testing Suite (50+ Tests)**
  - **Integration Tests:** Added `CultorControllerIntegrationTests` covering all CRUD operations (`GET`, `POST`, `PUT`, `DELETE`) with a full HTTP-to-Database flow using an H2 in-memory database.
  - **Unit Tests:** Implemented isolated unit tests using **Mockito** for core services (`CultorService`, `AuthenticationService`, `JwtService`), utilities (`DateValidator`), and JPA Specifications (`CultorSpecification`).
  - **Test Infrastructure:** Configured a dedicated `src/test/resources/application.properties` for isolated test environments.

- **🗄️ Professional Database Migration (Flyway)**
  - Integrated **Flyway** for robust schema version control.
  - Added migration scripts:
    - `V1__Create_Complete_Schema.sql`: Defines the full database schema with foreign keys and constraints.
    - `V2__Insert_Reference_Data.sql`: Populates lookup tables (Municipalities, Parishes, Art Categories) using ID-agnostic subqueries.
  - **Important:** `spring.jpa.hibernate.ddl-auto` is now set to `validate` in production to prevent accidental schema changes.

- **📚 Complete Code Documentation**
  - Added professional, standard **Javadoc** to 100% of the codebase (Controllers, Services, Entities, DTOs, Config).
  - Documented all configuration files (`docker-compose`, `.env`, `pom.xml`) to facilitate onboarding for new developers.

### 🔧 Changed

- **Refactored `CultorService`** to return `201 Created` (instead of `200 OK`) for successful POST requests, adhering to REST standards.
- **Enhanced `GlobalExceptionHandler`** to provide detailed validation errors (concatenating multiple field errors) and handle security exceptions (`401`, `403`) consistently.
- **Fixed Logic Bugs:** Corrected edge-case logic in `DateValidator` (birthday boundary) and JPA joins in `CultorSpecification` discovered during the testing phase.

### 🚨 Breaking Changes

- **Database Schema Control:** The application no longer auto-generates tables in production. You must use the provided Flyway migration scripts.
- **Strict Validation:** The API now enforces stricter validation rules for `Date` fields and relationships (e.g., Parish must belong to Municipality), returning `400 Bad Request` for violations that might have previously passed.