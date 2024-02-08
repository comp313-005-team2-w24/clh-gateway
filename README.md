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

# Bookstore API Endpoints

## Auth

- `POST /auth/createUser` - Create a new user. This endpoint allows for the creation of a new user account in the system. It typically requires user details such as username, password, and email.

- `POST /auth/login` - Authenticate a user and retrieve a token. This endpoint authenticates a user's credentials and returns a token for accessing protected routes.

- `GET /auth/validateToken?token={token}` - Validate a user's authentication token. This endpoint checks if the provided authentication token is valid and active.

## Author

- `POST /authors/` - Create a new author. This endpoint is used to add a new author to the bookstore system. It requires details of the author, such as name, biography, and avatar URL, in the request body.

- `GET /authors/` - Get all authors. This endpoint retrieves a list of all authors currently available in the bookstore system. It returns basic information about each author, including their ID, name, biography, and avatar URL.

- `GET /authors/id/{id}` - Get an author by ID. This endpoint retrieves detailed information about a specific author, identified by their unique ID. It includes the author's details along with a list of books associated with them.

- `PUT /authors/{id}/avatar` - Update an author's avatar URL by ID. This endpoint allows for updating the avatar URL of an existing author, identified by their unique ID. It requires the new avatar URL as a request parameter.

These endpoints provide a RESTful interface for managing users and authors within the bookstore system. Ensure proper authentication and authorization mechanisms are implemented to protect sensitive endpoints and data.

### Create Author
To create a new author, send a `POST` request to `/authors/` with a JSON body containing the author's details. Here is an example of the JSON request body:

```json
{
  "name": "John Doe",
  "biography": "John Doe is an acclaimed author known for his contributions to modern literature. He has won numerous awards for his work.",
  "avatar_url": "http://example.com/avatar/johndoe.jpg"
}
```

### Create Book
To add a new book to the system, send a POST request to /books/ with a JSON body detailing the book's information, including its title, description, ISBN, publication date, price, stock quantity, and associated author IDs. Here is an example of the JSON request body:
```json
{
  "title": "The Great Adventure",
  "description": "A captivating tale of exploration and discovery that takes readers on a thrilling journey across uncharted lands.",
  "isbn": "978-3-16-148410-0",
  "publicationDate": "2023-02-01",
  "price": 19.95,
  "stockQuantity": 100,
  "authorIds": [1, 2]
}
```

### Update Book

To update an existing book's details, send a `PUT` request to `/books/{id}`. It's important to note that the update operation relies on the `book_id` specified in the JSON payload, not on the ID in the URI. Therefore, the `{id}` in the URI `/books/{id}` should match the `book_id` in the JSON payload, or it can be a placeholder, as the actual book identification for update operations is extracted from the JSON body.

Here is an example of the JSON request body for updating a book:

```json
{
  "book_id": 1,
  "title": "The Great Adventure Revised",
  "description": "An updated description of the captivating tale, adding insights into the exploration and discoveries made.",
  "isbn": "978-3-16-148410-0",
  "publicationDate": "2023-03-01",
  "price": 21.95,
  "stockQuantity": 120,
  "authorIds": [1]
}
```

## Configuration

Application configurations are located in `src/main/resources/application.properties`.  
gRPC Hibernate service server env `BOOKSTORE_GRPC_HOST`  default _localhost_.   
gRPC Hibernate service server env `envBOOKSTORE_GRPC_PORT`  default _8082_  


## Contributing

Contributions to the CLH Gateway Service are welcome. Please follow the standard procedure for contributing to a GitHub repository:

1. Fork the repository.
2. Create a new branch for each feature or improvement.
3. Submit a pull request with a clear description of your changes.