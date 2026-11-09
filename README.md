# Movies API

A REST API for managing a movie collection and its reviews, built with **Spring Boot** and backed by a **PostgreSQL** database.
It supports full CRUD operations for movies and reviews, filtering by genre and rating, input validation, custom exception handling, interactive API documentation and unit tests.

---

## Tech Stack

- **Java 17+**
- **Spring Boot 4** (Web MVC, Data JPA, Validation)
- **PostgreSQL**
- **Flyway** (database migrations)
- **Hibernate / JPA**
- **springdoc-openapi** (Swagger UI)
- **JUnit 5, Mockito, AssertJ** (testing)
- **Maven**

---

## Features

- Create, read, update and delete movies (CRUD)
- Create, read, update and delete **reviews** for each movie (one-to-many relationship)
- Reviews are nested under their movie: `/movies/{movieId}/reviews`
- **Ownership check**: a review can only be updated or deleted through the movie it belongs to, otherwise the API returns `404`
- Filter movies by **genre** and/or **minimum rating**
- Data is stored in a real database, so it persists after restarts
- Database schema and sample data created automatically with **Flyway**
- Request validation with clear, field-level error messages
- **Custom exceptions** (`MovieNotFoundException`, `ReviewNotFoundException`) for missing resources
- **Global exception handling** with `@RestControllerAdvice`: errors are turned into proper HTTP responses in one place
- Layered architecture: **Controller → Service → Repository**
- DTOs (`MovieRequest` / `MovieResponse`, `ReviewRequest` / `ReviewResponse`) to keep the entities separate from the API
- Interactive API documentation with **Swagger UI**
- **21 automated tests**: unit tests for the service layer and web-layer tests for the controller

---

## How Errors Are Handled

The service layer decides whether a resource exists. If it doesn't, it throws a `MovieNotFoundException` or a `ReviewNotFoundException`.
The exception travels up to the `GlobalExceptionHandler`, which turns it into a `404 Not Found` response.

```
MovieService / ReviewService  →  throws MovieNotFoundException / ReviewNotFoundException
        ↓
Controllers                   →  no checks needed, stay clean
        ↓
GlobalExceptionHandler        →  returns 404 with a clear message
```

This keeps the controllers focused only on HTTP, and the "not found → 404" rule is written **once**, not in every endpoint.

The same mechanism is used for the ownership check: if a review exists but belongs to a different movie than the one in the URL, the service throws `ReviewNotFoundException`, because that review does not exist for that movie.

---

## Endpoints

### Movies

| Method | Endpoint | Description | Success | Error |
|--------|----------|-------------|---------|-------|
| `GET` | `/movies` | Get all movies | `200 OK` | - |
| `GET` | `/movies?genre=Drama` | Filter by genre (case-insensitive) | `200 OK` | - |
| `GET` | `/movies?minRating=8.5` | Filter by minimum rating | `200 OK` | - |
| `GET` | `/movies?genre=Drama&minRating=8.5` | Filter by both | `200 OK` | - |
| `GET` | `/movies/{id}` | Get one movie | `200 OK` | `404 Not Found` |
| `POST` | `/movies` | Create a movie | `201 Created` | `400 Bad Request` |
| `PUT` | `/movies/{id}` | Update a movie | `200 OK` | `400` / `404` |
| `DELETE` | `/movies/{id}` | Delete a movie | `204 No Content` | `404 Not Found` |

### Reviews

| Method | Endpoint | Description | Success | Error |
|--------|----------|-------------|---------|-------|
| `GET` | `/movies/{movieId}/reviews` | Get all reviews of a movie | `200 OK` | `404 Not Found` |
| `POST` | `/movies/{movieId}/reviews` | Add a review to a movie | `201 Created` | `400` / `404` |
| `PUT` | `/movies/{movieId}/reviews/{reviewId}` | Update a review | `200 OK` | `400` / `404` |
| `DELETE` | `/movies/{movieId}/reviews/{reviewId}` | Delete a review | `204 No Content` | `404 Not Found` |

---

## API Documentation

Once the application is running, the interactive **Swagger UI** is available at:

```
http://localhost:8080/swagger-ui/index.html
```

It lists all endpoints and lets you try them directly from the browser. The raw OpenAPI definition is available at:

```
http://localhost:8080/v3/api-docs
```

---

## Example Requests

**POST** `/movies`

```json
{
  "title": "Spirited Away",
  "genre": "Animation",
  "rating": 8.6,
  "releaseYear": 2001
}
```

**Response:** `201 Created`

```json
{
  "id": 5,
  "title": "Spirited Away",
  "genre": "Animation",
  "rating": 8.6,
  "releaseYear": 2001
}
```

**POST** `/movies/5/reviews`

```json
{
  "rating": 9,
  "comment": "Beautiful animation and a great story."
}
```

**Response:** `201 Created`

---

## Error Responses

### Resource not found: `404 Not Found`

**GET** `/movies/999`

```json
{
  "error": "Movie with id 999 not found"
}
```

The same `404` is returned when a review does not exist, or when it exists but belongs to a different movie than the one in the URL.

### Invalid input: `400 Bad Request`

Movie rules:

| Field | Rule |
|-------|------|
| `title` | Required, max 100 characters |
| `genre` | Required, max 50 characters |
| `rating` | Between 0.0 and 10.0 |
| `releaseYear` | 1880 or later |

```json
{
  "title": "must not be blank",
  "genre": "size must be between 0 and 50",
  "rating": "must be less than or equal to 10.0",
  "releaseYear": "must be greater than or equal to 1880"
}
```

---

## Testing

The project has **21 tests**. The service and controller layers are each tested on their own, with the layer below replaced by a **mock**, so the tests run fast and don't need a database.

| Layer | Test class | Tools | What is tested |
|-------|-----------|-------|----------------|
| Service | `MovieServiceTest` (11 tests) | JUnit 5, Mockito, AssertJ | Business logic: finding, creating, updating, deleting and filtering movies, and throwing `MovieNotFoundException` |
| Controller | `MovieControllerTest` (9 tests) | `@WebMvcTest`, `MockMvc`, `@MockitoBean` | HTTP behaviour: status codes, JSON responses, validation errors (400) and not-found errors (404) through the `GlobalExceptionHandler` |
| Application | `MoviesApplicationTests` (1 test) | `@SpringBootTest` | The application context starts correctly |

Both **happy paths** (everything works) and **sad paths** (missing movie, invalid input) are covered.
Each test follows the **Given / When / Then** structure.

Run all tests:

**Windows:**
```
.\mvnw.cmd test
```
**Linux / macOS:**
```
./mvnw test
```

---

## How to Run

### 1. Requirements
- Java 17 or newer
- PostgreSQL running on `localhost:5432`

### 2. Create the database
In pgAdmin (or psql), create an empty database called:
```
movies_db
```

### 3. Set the database password
The password is **not** stored in the code. It is read from an environment variable.

**Windows (PowerShell):**
```
setx DB_PASSWORD "your_password"
```
Then restart your terminal / IDE.

**Linux / macOS:**
```
export DB_PASSWORD=your_password
```

### 4. Run the application
**Windows:**
```
.\mvnw.cmd spring-boot:run
```
**Linux / macOS:**
```
./mvnw spring-boot:run
```

Flyway will automatically create the database tables and insert sample movies.
The API will be available at `http://localhost:8080/movies`, and the Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

---

## Project Structure

```
src/main/java/com/dimitra/movies
├── controller   → REST endpoints (MovieController, ReviewController)
├── service      → Business logic (MovieService, ReviewService)
├── repository   → Database access (MovieRepository, ReviewRepository)
├── entity       → JPA entities (Movie, Review)
├── dto          → Request/Response records
└── exception    → Custom exceptions + GlobalExceptionHandler

src/main/resources
├── application.properties
└── db/migration → Flyway SQL scripts

src/test/java/com/dimitra/movies
├── controller   → Web-layer tests (MovieControllerTest)
└── service      → Unit tests (MovieServiceTest)
```

---

## What I Learned

- Connecting Spring Boot to a real PostgreSQL database
- Managing the database schema with Flyway migrations
- Using Spring Data JPA derived queries (e.g. `findByGenreIgnoreCaseAndRatingGreaterThanEqual`)
- Modelling a **one-to-many relationship** (Movie → Reviews) and designing the tables in SQL first
- Designing **nested REST routes** (`/movies/{movieId}/reviews`) and checking that a child resource really belongs to its parent
- Working with `Optional` and `.orElseThrow()`
- Creating **custom unchecked exceptions** and handling them globally with `@ExceptionHandler`
- Keeping business logic in the service and the controllers free of checks
- Choosing correct HTTP status codes (200, 201, 204, 400, 404)
- Validating input with Jakarta Bean Validation
- Writing **unit tests** with JUnit 5 and **mocking** dependencies with Mockito
- Testing the web layer with `@WebMvcTest` and `MockMvc`: status codes, JSON responses and validation
- Testing both happy paths and sad paths, and choosing the right layer for each test
- Why **constructor injection** makes classes easy to test
- Keeping secrets out of the code with environment variables
- Documenting a REST API with **OpenAPI / Swagger UI** using springdoc
- Working with Git branches, pull requests and resolving merge conflicts

---

## Author

**Dimitra** · [GitHub](https://github.com/Dodopoud)
