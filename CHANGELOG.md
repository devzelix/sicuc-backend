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
