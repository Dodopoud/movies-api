# Movies API

A REST API for managing a movie collection, built with Spring Boot and PostgreSQL.

**Tech stack:** Java 17, Spring Boot 4, Spring Data JPA, PostgreSQL, Flyway, JUnit 5, Mockito, Maven

## Features

- Full CRUD for movies, with filtering by genre and minimum rating
- Layered architecture (Controller, Service, Repository) with DTOs
- Database schema and sample data managed with Flyway
- Input validation with field-level error messages
- Custom `MovieNotFoundException` handled globally, returning `404 Not Found`
- Unit tests for the service layer with a mocked repository

## Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/movies` | Get all movies (optional `?genre=` and `?minRating=`) |
| `GET` | `/movies/{id}` | Get one movie |
| `POST` | `/movies` | Create a movie |
| `PUT` | `/movies/{id}` | Update a movie |
| `DELETE` | `/movies/{id}` | Delete a movie |

## Example Request

`POST /movies`

```json
{
  "title": "Spirited Away",
  "genre": "Animation",
  "rating": 8.6,
  "releaseYear": 2001
}
```

## How to Run

**Requirements:** Java 17+ and PostgreSQL

1. Create an empty PostgreSQL database called `movies_db`
2. Set the database password as an environment variable: `DB_PASSWORD`
3. Run `./mvnw spring-boot:run` (Windows: `.\mvnw.cmd spring-boot:run`)

Flyway creates the table and sample data automatically. The API runs at `http://localhost:8080/movies`.

Run the tests with `./mvnw test`.

## Author

Dimitra · [GitHub](https://github.com/Dodopoud)
