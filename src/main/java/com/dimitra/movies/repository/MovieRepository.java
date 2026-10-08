package com.dimitra.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.dimitra.movies.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByGenreIgnoreCase(String genre);
    List<Movie> findByRatingGreaterThanEqual(double minRating);
    List<Movie> findByGenreIgnoreCaseAndRatingGreaterThanEqual(String genre, double minRating);

}
