# Movies API

A REST API for managing a movie collection, built with **Spring Boot** and backed by a **PostgreSQL** database.
It supports full CRUD operations, filtering by genre and rating, input validation, custom exception handling and unit tests.

---

## Tech Stack

- **Java 17+**
- **Spring Boot 4** (Web MVC, Data JPA, Validation)
- **PostgreSQL**
- **Flyway** (database migrations)
- **Hibernate / JPA**
- **JUnit 5, Mockito, AssertJ** (testing)
- **Maven**

---

## Features

- Create, read, update and delete movies (CRUD)
- Filter movies by **genre** and/or **minimum rating**
- Data is stored in a real database, so it persists after restarts
- Database schema and sample data created automatically with **Flyway**
- Request validation with clear, field-level error messages
- **Custom exception** (`MovieNotFoundException`) for missing movies
- **Global exception handling** with `@RestControllerAdvice`: errors are turned into proper HTTP responses in one place
- Layered architecture: **Controller → Service → Repository**
- DTOs (`MovieRequest` / `MovieResponse`) to keep the entity separate from the API
- **21 automated tests**: unit tests for the service layer and web-layer tests for the controller

---

## How Errors Are Handled

The service layer decides whether a movie exists. If it doesn't, it throws a `MovieNotFoundException`.
The exception travels up to the `GlobalExceptionHandler`, which turns it into a `404 Not Found` response.

```
MovieService       →  throws MovieNotFoundException
        ↓
MovieController    →  no checks needed, stays clean
        ↓
GlobalExceptionHandler  →  returns 404 with a clear message
```

This keeps the controller focused only on HTTP, and the "not found → 404" rule is written **once**, not in every endpoint.

---

## Endpoints

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

---

## Example Request

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

---

## Error Responses

### Movie not found: `404 Not Found`

**GET** `/movies/999`

```json
{
  "error": "Movie with id 999 not found"
}
```

### Invalid input: `400 Bad Request`

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

Flyway will automatically create the `movie` table and insert sample movies.
The API will be available at `http://localhost:8080/movies`.

---

## Project Structure

```
src/main/java/com/dimitra/movies
├── controller   → REST endpoints (MovieController)
├── service      → Business logic (MovieService)
├── repository   → Database access (MovieRepository)
├── entity       → JPA entity (Movie)
├── dto          → Request/Response records
└── exception    → MovieNotFoundException + GlobalExceptionHandler

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
- Working with `Optional` and `.orElseThrow()`
- Creating a **custom unchecked exception** and handling it globally with `@ExceptionHandler`
- Keeping business logic in the service and the controller free of checks
- Choosing correct HTTP status codes (200, 201, 204, 400, 404)
- Validating input with Jakarta Bean Validation
- Writing **unit tests** with JUnit 5 and **mocking** dependencies with Mockito
- Testing the web layer with `@WebMvcTest` and `MockMvc`: status codes, JSON responses and validation
- Testing both happy paths and sad paths, and choosing the right layer for each test
- Why **constructor injection** makes classes easy to test
- Keeping secrets out of the code with environment variables

---

## Next Steps

- [x] Custom exceptions (`MovieNotFoundException`)
- [x] Unit tests for the service and controller layers
- [ ] Pagination and sorting
- [ ] A `Review` entity with a one-to-many relationship
- [ ] A consistent error format for all errors (`ProblemDetail`)
- [ ] Integration tests

---

## Author

**Dimitra** · [GitHub](https://github.com/Dodopoud)
