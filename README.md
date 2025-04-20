# Task Management Service

A Task Management REST API built using Spring Boot. This project follows a layered architecture for clarity and maintainability.

## Architecture Overview

- **REST API Layer** – Exposes endpoints for task operations
- **Service Layer** – Handles business logic
- **Repository Layer** – Manages database operations
- **Security Layer** – Secures APIs using JWT Authentication
- **Database** – Uses H2 for development and PostgreSQL for production

## Tech Stack

| Component      | Technology                      |
|----------------|----------------------------------|
| Framework      | Spring Boot 3.4.4                |
| Database       | H2 (development), PostgreSQL     |
| ORM            | Spring Data JPA                  |
| Security       | Spring Security + JWT            |
| Testing        | JUnit 5, Mockito, Testcontainers |
| API Docs       | OpenAPI (Swagger UI)             |
| Build Tool     | Maven                            |

## Features Covered
* Task management
* User management
* Secure access

## Technical aspects
* Application security
* CRUD operations
* Database Integration
* Send email

## Libraries and tools used
* Spring security & JWT
* MapStruct for conversions between Entities and DTOs
* Docker
* PostgreSQL

## 🐳 PostgreSQL Integration (via Docker)

This project integrates PostgreSQL using Docker. Follow the steps below to set up and connect PostgreSQL with your Spring Boot application:

### 1. Add `docker-compose.yml`

A `docker-compose.yml` file is included at the root of this project. It defines services for:
- PostgreSQL database (`postgres`)
- PgAdmin web client (`pgadmin`)

### 2. Start Containers

- For the first-time setup, run:
  ```bash
  docker-compose up -d
  
- If the system has been restarted, run:
  ```bash
  docker start springboot_postgres springboot_pgadmin

### 3. Useful Docker Commands

Use the following commands to manage your PostgreSQL and pgAdmin containers:

| Command | Description |
|--------|-------------|
| `docker-compose up -d` | Starts the containers in detached mode |
| `docker start springboot_postgres springboot_pgadmin` | Starts the containers after a system restart |
| `docker stop springboot_postgres springboot_pgadmin` | Stops the containers |
| `docker ps` | Lists running containers |
| `docker logs springboot_postgres` | Shows logs for the PostgreSQL container |
| `docker-compose down` | Stops and removes the containers and their associated volumes |

---

### 4. Access pgAdmin

Follow these steps to manage PostgreSQL visually using pgAdmin:

1. Open your browser and navigate to: [http://localhost:6070](http://localhost:6070)

2. Login with:
  - **Email**: `admin@admin.com`
  - **Password**: `admin123`

3. Add a new server in pgAdmin:
  - **Name**: `SpringBoot PostgreSQL` *(or any name of your choice)*
  - **Host**: `postgres` *(Docker service name, not `localhost`)*
  - **Port**: `5432`
  - **Username**: `dbuser`
  - **Password**: `tmdbuser123`
  - **Database**: `taskdb`

---

### 5. Spring Boot Configuration for PostgreSQL

Update `application.properties`) to connect the Spring Boot app to the PostgreSQL database:


This project is licensed under the MIT License.

