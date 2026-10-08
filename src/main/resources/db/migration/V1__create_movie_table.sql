CREATE TABLE movie (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL, 
    genre VARCHAR(50) NOT NULL,
    rating DOUBLE PRECISION NOT NULL, 
    release_year INTEGER NOT NULL
);

INSERT INTO movie (title, genre, rating, release_year) VALUES
    ('Titanic', 'action', 8.5, 1999),
    ('The Godfather', 'Drama', 9.2, 1972),
    ('Toy Story', 'Animation', 8.3, 1995),
    ('Interstellar', 'Sci-Fi', 8.7, 2014);