# Cultist Registration Backend

## Table of Contents

- [Project Description](#project-description)
- [Technologies Used](#technologies-used)
- [How to Get Started](#how-to-get-started)
  - [Prerequisites](#prerequisites)
  - [Setup Instructions](#setup-instructions)
- [How to Run the Application](#how-to-run-the-application)
  - [Using Maven](#using-maven)
  - [Using Gradle](#using-gradle)
  - [Using Docker Compose](#using-docker-compose)
- [Accessing the Application](#accessing-the-application)
- [API Endpoints Overview](#api-endpoints-overview)
- [Testing the API](#testing-the-api)
- [License](#license)

## Project Description

This backend application supports the Cultist Registration system for Secretaría de Cultura Carabobo. Built with Spring Boot, it provides RESTful APIs to manage cultists and related entities such as municipalities, parishes, art categories, and art disciplines. It includes features for filtering cultists by various criteria, validating input data, and handling exceptions gracefully.

## Technologies Used

- Java 21+
- Spring Boot 3.4.6+
- Spring Data JPA
- Hibernate ORM
- Jakarta Persistence (JPA)
- Jakarta Validation API
- Maven or Gradle build tools
- Docker & Docker Compose

## How to Get Started

### Prerequisites

- Java Development Kit (JDK) 21 or later
- Maven or Gradle installed
- Docker & Docker Compose installed
- A configured relational database (unless you plan to use Docker Compose to run a database container)

### Setup Instructions

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/cultist-registration-backend.git
   cd cultist-registration-backend
   ```

2. Create a `.env` file in the root of the project with your environment variables:

   ```properties
   SPRING_PROFILES_ACTIVE=dev
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/your_database_name
   SPRING_DATASOURCE_USERNAME=your_database_user
   SPRING_DATASOURCE_PASSWORD=your_database_password
   ```

## How to Run the Application

### Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Using Gradle

```bash
./gradlew build
./gradlew bootRun
```

### Using Docker Compose

You can also run the entire application stack (backend + database) using Docker Compose.

1. Ensure Docker and Docker Compose are installed on your machine.

2. Start the application and services:

   ```bash
   docker compose up -d
   ```

3. To stop the containers:

   ```bash
   docker compose down
   ```

## Accessing the Application

Once started, the backend server will be running at:

```bash
http://localhost:8080
```

## API Endpoints Overview

| Method | Endpoint         | Description                         |
| ------ | ---------------- | ----------------------------------- |
| GET    | /cultists        | List cultists with optional filters |
| POST   | /cultists        | Register a new cultist              |
| GET    | /municipalities  | Get all municipalities              |
| GET    | /parishes        | Get all parishes                    |
| GET    | /art-categories  | Get all art categories              |
| GET    | /art-disciplines | Get all art disciplines             |

## Testing the API

You can use tools like [Postman](https://www.postman.com/) or `curl` to test the endpoints. For example:

```bash
curl -X GET http://localhost:8080/cultists
```

## License

This project is licensed under the [MIT License](LICENSE).
