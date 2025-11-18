<div align="center">
  <img src="https://placehold.co/1200x300/2E3440/E5E9F0/png?text=SICUC+Backend&font=lato" alt="Project Banner">
  <br/>
  <br/>
  <p>
    <strong>REST API for the Unified Cultural Information System of Carabobo</strong>
  </p>
  <p>
    A robust, secure, and scalable backend for managing the census of cultural workers (cultors).
  </p>
  <p>
    <a href="#-about-the-project">About</a> •
    <a href="#-getting-started">Getting Started</a> •
    <a href="#-usage">Usage</a> •
    <a href="#-configuration">Configuration</a> •
    <a href="#-deployment">Deployment</a>
  </p>
  <p>
    <img src="https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=flat-square&logo=spring" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Java-21-blue?style=flat-square&logo=openjdk" alt="Java 21">
    <img src="https://img.shields.io/badge/Docker-20.10-blue?style=flat-square&logo=docker" alt="Docker">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square" alt="License: MIT">
  </p>
</div>

---

## 🚀 About The Project

The **SICUC Backend** is the server-side application for the *Sistema Unificado de Información Cultural del Estado Carabobo (SICUC)*. It is designed to manage a comprehensive database of "cultors"—a term for artists, artisans, and cultural practitioners.

The system provides:
- ✅ Secure, role-based access using JWT (JSON Web Tokens).
- ✅ Complete CRUD (Create, Read, Update, Delete) functionality for cultors, geographical data, and art disciplines.
- ✅ Advanced search and filtering capabilities across multiple fields.
- ✅ A containerized environment using Docker for consistent development and deployment.
- ✅ Database schema management through Flyway migrations.

---

## 🛠️ Built With

This project is built with modern, robust technologies.

<p align="center">
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT">
</p>

---

## 🏁 Getting Started

Follow these instructions to get a local copy up and running for development.

### Prerequisites

Ensure you have the following tools installed on your system:
- **Java Development Kit (JDK) 21**
- **Apache Maven**
- **Docker** and **Docker Compose**

### Installation

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/your-username/sicuc-backend.git
    cd sicuc-backend
    ```

2.  **Create the environment configuration file:**
    Duplicate the `.env.template` file and rename it to `.env`.
    ```sh
    cp .env.template .env
    ```
    Open the new `.env` file and customize the variables as needed. At a minimum, review the `JWT_SECRET_KEY` and database credentials. See the [Configuration](#-configuration) section for more details.

---

## ▶️ Usage

### Running for Development

The project is configured to run with Docker Compose, which orchestrates the backend application and the MySQL database containers.

To start the development environment:
```sh
docker-compose -f docker-compose.dev.yml up --build
```
- The backend will be available at `http://localhost:8080`.
- The API is served under the `/api/v1` path by default.
- The service supports hot-reloading for development, but you may need to manually restart the service or re-run the command if you make changes to dependencies in `pom.xml`.

### Running Tests

To execute the suite of unit and integration tests, run the following Maven command:
```sh
./mvnw test
```
This command uses the Maven Wrapper included in the repository and runs tests using an in-memory H2 database.

---

## 🗺️ API Endpoints Overview

All endpoints are prefixed with the base path defined in your `.env` file (default is `/api/v1`).

-   `POST /auth/login`: Authenticates a user and returns an access and refresh token.
-   `POST /auth/refresh`: Issues a new access token using a valid refresh token.
-   `GET /cultors`: Retrieves a paginated list of cultors with powerful filtering options.
-   `GET /cultors/{id}`: Retrieves a single cultor by their ID.
-   `POST /cultors`: Creates a new cultor.
-   `PUT /cultors/{id}`: Updates an existing cultor.
-   `DELETE /cultors/{id}`: Deletes a cultor.
-   **Other Endpoints**: The API also includes endpoints for managing `municipalities`, `parishes`, `art-categories`, and `art-disciplines`.

---

## ⚙️ Configuration

The application is configured using environment variables located in the `.env` file. Click to expand the table of variables.

<details>
<summary><strong>Environment Variables</strong></summary>
<br>

| Variable                       | Description                                                                                             | Default Value                     |
| ------------------------------ | ------------------------------------------------------------------------------------------------------- | --------------------------------- |
| `API_BASE_PATH`                | Base context path for the REST API.                                                                     | `/api/v1`                         |
| `SPRING_PROFILES_ACTIVE`       | The active Spring profile (`dev` or `prod`).                                                            | `dev`                             |
| `SPRING_JPA_HIBERNATE_DDL_AUTO`| Hibernate DDL policy. Use `update` for dev, `validate` for prod.                                        | `update`                          |
| `SPRING_DATASOURCE_URL`        | JDBC connection URL for the database.                                                                   | `jdbc:mysql://sicuc-db:3306/sicuc_db` |
| `SPRING_DATASOURCE_USERNAME`   | Database user.                                                                                          | `sicuc_user`                      |
| `SPRING_DATASOURCE_PASSWORD`   | Database password.                                                                                      | `sicuc_password_dev`              |
| `MYSQL_ROOT_PASSWORD_SECRET`   | Root password for the MySQL container. Used by Docker Compose on first run.                             | `my-strong-root-password`         |
| `JWT_SECRET_KEY`               | **CRITICAL:** A long, random, Base64-encoded string for signing JWTs. **Change this for production.**    | `your-ultra-secure-base64-secret-key` |
| `JWT_ACCESS_TOKEN_EXPIRATION`  | Expiration time for access tokens in milliseconds.                                                      | `3600000` (1 hour)                |
| `JWT_REFRESH_TOKEN_EXPIRATION` | Expiration time for refresh tokens in milliseconds.                                                     | `604800000` (7 days)              |

</details>

---

## 🚢 Deployment

The `Dockerfile` uses a multi-stage build to create a minimal, secure, and efficient production image.

### Building the Production Image

To build the final production-ready Docker image, run:
```sh
docker build -t sicuc-backend:prod .
```
This command executes both the `builder` and `runtime` stages defined in the `Dockerfile`, resulting in a lightweight image containing only the JRE and the application JAR.

### Running in Production

The `docker-compose.prod.yml` file is designed to run the application in a production environment.

1.  **Ensure your `.env` file is configured for production:**
    -   Set `SPRING_PROFILES_ACTIVE` to `prod`.
    -   Set `SPRING_JPA_HIBERNATE_DDL_AUTO` to `validate`.
    -   Use strong, non-default passwords and a secure `JWT_SECRET_KEY`.

2.  **Start the production services:**
    ```sh
    docker-compose -f docker-compose.prod.yml up -d
    ```
    This will start the `sicuc-backend` and `sicuc-db` services in detached mode.

---

## 📄 License

This project is distributed under the MIT License. See the `LICENSE` file for more information.
