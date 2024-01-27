# CLH Gateway Service

## Overview

CLH Gateway Service is a Spring Boot application that provides gateway functionalities with gRPC integration. It handles user authentication and validation processes, connecting to a gRPC service for backend operations.

## Features

- User authentication and token validation.
- Communication with gRPC services for user operations.
- Containerization support with Jib.
- Integration testing using Testcontainers.

## Prerequisites

- Java 17
- Maven or Gradle (build tool)
- Docker (for containerization and testing with Testcontainers)

## Getting Started

### Clone the repository

```bash
git clone [your-repository-url]
cd clh-gateway
```

### Building the application

You can build the application using either Maven or Gradle. Here's how you can do it with Gradle:

```bash
./gradlew build
```

### Running the application

To run the application:

```bash
./gradlew bootJar
java -jar build/libs/gateway-0.0.1-SNAPSHOT.jar
```

The service will start on port 8081, as specified in the Spring Boot configuration.

### Building and pushing Docker image

The project is configured to use Jib for building Docker images. To build and push the image to a registry:

```bash
./gradlew jib
```

Ensure you have configured the `jib` plugin in your `build.gradle` file with your image name and registry.

## Testing

The project uses Testcontainers for integration testing. To run tests:

```bash
./gradlew test
```

Make sure Docker is running on your machine as Testcontainers requires it to launch containers for testing.

## API Endpoints

## Auth
- POST `/auth/createUser` - Create a new user.
- POST `/auth/login` - Authenticate a user and retrieve a token.
- GET `/auth/validateToken?token={token}` - Validate a user's authentication token.

## Author
- POST `/authors/` - Create a new author.
- GET `/authors/` - Get all authors.

## Configuration

Application configurations are located in `src/main/resources/application.properties`. Modify them as needed for your environment.

## Contributing

Contributions to the CLH Gateway Service are welcome. Please follow the standard procedure for contributing to a GitHub repository:

1. Fork the repository.
2. Create a new branch for each feature or improvement.
3. Submit a pull request with a clear description of your changes.