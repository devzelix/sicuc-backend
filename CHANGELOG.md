## [1.1.0] - 2025-05-23

### ✨ Added

- The `createdAt` field was added to the `Cultor` entity. This field is automatically set when persisting to the database.

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
