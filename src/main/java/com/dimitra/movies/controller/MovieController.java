package com.dimitra.movies.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dimitra.movies.dto.MovieRequest;
import com.dimitra.movies.dto.MovieResponse;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.service.MovieService;

import jakarta.validation.Valid;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController (MovieService movieService){
        this.movieService = movieService;    }

    @GetMapping("/movies")
    public List<MovieResponse> getMovies(@RequestParam (required = false) String genre,
                                         @RequestParam(required = false) Double minRating){
    
          return movieService.search(genre, minRating).stream().map(MovieResponse::from).toList();                                  
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id){
        Movie movie = movieService.findById(id);
        if (movie == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(MovieResponse.from(movie));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable Long id){
        if (!movieService.deleteMovieById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request){
        Movie saved = movieService.createMovie(request); 
        return ResponseEntity.status(HttpStatus.CREATED).body(MovieResponse.from(saved));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request){
        
        Movie updated = movieService.updateMovie(id, request);
        if (updated == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(MovieResponse.from(updated));
    }
}
