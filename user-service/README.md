# Auth-Service: A Reactive Microservice

Welcome to the Auth-Service, a modern, reactive microservice built with Spring Boot and designed according to the principles of Hexagonal Architecture. This service provides a robust and scalable solution for user management, including registration and authentication, using MongoDB and integrating with Spring Cloud Eureka for service discovery.

## 🚀 Project Overview

The Auth-Service is a foundational component of a distributed system, responsible for handling all user-related operations. It is built on a fully reactive stack, ensuring high performance and resilience. The service registers itself with a Eureka server, making it discoverable by other services in the ecosystem. It exposes a RESTful API for user registration and login, secured with JSON Web Tokens (JWT).

## 🏛️ Architectural Design: Hexagonal Architecture

This project implements the **Hexagonal (Ports and Adapters) Architecture** to ensure a clear separation of concerns and to isolate the core business logic from external technologies and frameworks.

-   **`domain`**: The heart of the application. It contains the core business models, logic, and the "ports" (interfaces) that define the contract for external interactions.
-   **`application`**: This module orchestrates the business logic by implementing the input ports and using the output ports.
-   **`infrastructure`**: This layer contains the "adapters" that implement the ports.
    -   **`entry-points`**: `reactive-web` (Spring WebFlux REST API).
    -   **`driven-adapters`**: `persistence` (MongoDB), `security` (JWT), `kafka-sender`, `logger`, and `aop`.
-   **`run`**: The runnable module that assembles and configures the final application.

## ✨ Key Features

-   **Reactive and Non-Blocking:** Built with Spring WebFlux and Project Reactor.
-   **Service Discovery:** Registers with **Spring Cloud Eureka** to be discoverable within a microservices' architecture.
-   **Secure Authentication:** Implements JWT-based authentication with Spring Security.
-   **User Management:** Provides RESTful endpoints for user registration and login.
-   **NoSQL Database:** Uses Spring Data MongoDB Reactive for data persistence.
-   **Asynchronous Messaging:** Integrates with Apache Kafka for event-driven communication.
-   **API Documentation:** Generates interactive API documentation with Springdoc OpenAPI.
-   **Monitoring:** Exposes `health` and `prometheus` endpoints via Spring Boot Actuator.
-   **Clean Architecture:** Follows the Hexagonal Architecture for maintainability and testability.

## 🛠️ Technology Stack

-   **Frameworks:** Spring Boot 3, Spring WebFlux, Spring Security, Spring Data MongoDB Reactive
-   **Service Discovery:** Spring Cloud Eureka
-   **Programming Language:** Java 17
-   **Reactive Programming:** Project Reactor
-   **Database:** MongoDB
-   **Messaging:** Apache Kafka
-   **Authentication:** JSON Web Tokens (JWT)
-   **Build Tool:** Gradle
-   **API Documentation:** Springdoc OpenAPI
-   **Utilities:** Lombok, MapStruct

## 📖 API Documentation

The API is documented using OpenAPI 3. Once the application is running, you can access the interactive Swagger UI. Since the service runs on a random port, you will need to check the application logs or the Eureka dashboard to find the exact address.

The documentation will be available at: `http://<host>:<random_port>/swagger-doc`

## 🏁 Getting Started

### Prerequisites

-   JDK 17 or later
-   Gradle 8.x
-   Docker and Docker Compose
-   A running **Eureka Server** instance.

### 1. Build the Project

```bash
./gradlew build
```

### 2. Run External Services with Docker

The required external services (MongoDB and Kafka) can be started using the provided Docker Compose file:

```bash
docker-compose -f deployment/docker-compose.yml up -d
```

### 3. Run the Eureka Server

Ensure you have a Eureka server running and accessible. By default, the service will try to connect to `http://localhost:8761/eureka/`.

### 4. Configure the Application

The application is configured using environment variables. The following variables are required:

-   **Eureka:**
    -   `EUREKA_CLIENT_SERVICE-URL_DEFAULTZONE`: The URL of the Eureka server. Defaults to `http://localhost:8761/eureka/`.
-   **MongoDB:**
    -   `ADAPTERS_PERSISTENCE_HOST`: The hostname of the MongoDB database.
    -   `ADAPTERS_PERSISTENCE_PORT`: The port of the MongoDB database.
    -   `ADAPTERS_PERSISTENCE_DATABASE`: The name of the database.
    -   `ADAPTERS_PERSISTENCE_USERNAME`: The username for the database.
    -   `ADAPTERS_PERSISTENCE_PASSWORD`: The password for the database.
    -   `ADAPTERS_PERSISTENCE_AUTHSOURCE`: The authentication database (e.g., `admin`).
-   **Kafka:**
    -   `ADAPTERS_KAFKA_PRODUCER_BOOTSTRAP-SERVERS`: The address of the Kafka broker(s).
### 5. Run the Application

Once the services are running and the environment is configured, you can start the application:

```bash
./gradlew :run:bootRun
```

The service will start on a **random port**. Check the application logs or the Eureka dashboard for the assigned port.

## 🧪 Running Tests

To run the unit and integration tests, use the following command:

```bash
./gradlew test
```
