# ⚙️ Carabobo Cultors Information System Backend

This backend application supports the Carabobo Cultors Information System for Secretariat Of Culture Of Carabobo. Built with Spring Boot, it provides RESTful APIs to manage cultors and related entities such as municipalities, parishes, art categories, and art disciplines. It includes features for filtering cultors by various criteria, validating input data, and handling exceptions gracefully.

## Table of Contents

- [🚀 Technologies Used](#🚀-technologies-used)
- [🛠️ How to Get Started](#🛠️-how-to-get-started)
  - [📋 Prerequisites](#📋-prerequisites)
  - [⚙️ Setup Instructions](#⚙️-setup-instructions)
- [▶️ How to Run the Application](#▶️-how-to-run-the-application)
  - [🐳 Using Docker Compose (Database Only)](#🐳-using-docker-compose-database-only)
  - [📦 Running the Spring Boot Application](#📦-running-the-spring-boot-application)
    - [🔧 Using Maven](#🔧-using-maven)
    - [🔧 Using Gradle](#🔧-using-gradle)
- [🌐 Accessing the Application](#🌐-accessing-the-application)
- [📡 API Endpoints Overview](#📡-api-endpoints-overview)
- [🧪 Testing the API](#🧪-testing-the-api)
- [📄 License](#📄-license)

## 🚀 Technologies Used

- Java 21+
- Spring Boot 3.5.0+
- Spring Data JPA
- Hibernate ORM
- Jakarta Persistence (JPA)
- Jakarta Validation API
- Maven or Gradle build tools
- Docker & Docker Compose

## 🛠️ How to Get Started

### 📋 Prerequisites

- Java Development Kit (JDK) 21 or later
- Maven or Gradle installed
- Docker & Docker Compose installed
- A configured relational database (unless you plan to use Docker Compose to run a database container)

### ⚙️ Setup Instructions

1. Clone the repository:

   ```bash
   git clone https://github.com/devzelix/sicuc-backend.git
   cd sicuc-backend
   ```

2. Create a `.env` file in the root of the project with your environment variables:

   ```properties
   API_BASE_PATH=your_base_path
   SPRING_PROFILES_ACTIVE=dev
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/your_database_name
   SPRING_DATASOURCE_USERNAME=your_database_user
   SPRING_DATASOURCE_PASSWORD=your_database_password
   ```

## ▶️ How to Run the Application

### 🐳 Using Docker Compose (Database Only)

The Docker Compose setup provided only starts the database container. You need to run your Spring Boot application separately, connecting it to the running database.

#### Steps:

1. Make sure Docker and Docker Compose are installed on your machine.

2. Start the database container:

   ```bash
   docker compose up -d
   ```

3. Once the database is running, run your Spring Boot application separately using Maven or Gradle (see below).

4. To stop the database container:

   ```bash
   docker compose down
   ```

Alternatively, you can connect your Spring Boot application to your own existing database without using Docker Compose.

### 📦 Running the Spring Boot Application

You can start the Spring Boot app directly on your machine after the database is up and running.

#### 🔧 Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

### 🔧 Using Gradle

```bash
./gradlew build
./gradlew bootRun
```

## 🌐 Accessing the Application

Once started, the backend server will be running at:

```bash
http://localhost:8080
```

## 📡 API Endpoints Overview

| Method | Endpoint         | Description                        |
| ------ | ---------------- | ---------------------------------- |
| GET    | /cultors         | List cultors with optional filters |
| POST   | /cultors         | Register a new cultors             |
| GET    | /municipalities  | Get all municipalities             |
| GET    | /parishes        | Get all parishes                   |
| GET    | /art-categories  | Get all art categories             |
| GET    | /art-disciplines | Get all art disciplines            |

## 🧪 Testing the API

You can use tools like [Postman](https://www.postman.com/) or `curl` to test the endpoints. For example:

```bash
curl -X GET http://localhost:8080/base_path/cultors
```

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
