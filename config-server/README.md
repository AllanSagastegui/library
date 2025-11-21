# Eureka Server

This project implements a Eureka Server using Spring Cloud Netflix. It acts as a service registry for a microservices' architecture.

## Prerequisites

*   Java 17
*   Gradle

## Installation and Running

1.  Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2.  Navigate to the project directory:
    ```bash
    cd eureka-server
    ```
3.  Build the project:
    ```bash
    ./gradlew build
    ```
4.  Run the application:
    ```bash
    ./gradlew bootRun
    ```

The server will start on port 8761.

## Configuration

The application is configured in `src/main/resources/application.yml`:

```yaml
server:
  port: 8761
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  instance:
    hostname: eureka
```

*   `server.port`: The port on which the Eureka server runs.
*   `eureka.client.register-with-eureka`: Since this is the server, it does not need to register with itself.
*   `eureka.client.fetch-registry`: The server does not need to fetch the registry from another instance.
*   `eureka.instance.hostname`: The hostname of the Eureka server instance.

## Usage

Once the server is running, you can access the Eureka dashboard in your browser at [http://localhost:8761](http://localhost:8761).

## Deployment

The `deployment` directory contains files related to the deployment of the application.
