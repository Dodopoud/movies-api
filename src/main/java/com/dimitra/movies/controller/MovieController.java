package com.dimitra.movies.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dimitra.movies.dto.MovieRequest;
import com.dimitra.movies.dto.MovieResponse;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.service.MovieService;

import jakarta.validation.Valid;

@RequestMapping("/movies")
@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController (MovieService movieService){
        this.movieService = movieService;    
    }

    @GetMapping
    public List<MovieResponse> getMovies(@RequestParam (required = false) String genre,
                                         @RequestParam(required = false) Double minRating){
    
          return movieService.search(genre, minRating).stream().map(MovieResponse::from).toList();                                  
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id){
        Movie movie = movieService.findById(id);
     
        return ResponseEntity.ok(MovieResponse.from(movie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable Long id){
        movieService.deleteMovieById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request){
        Movie saved = movieService.createMovie(request); 
        return ResponseEntity.status(HttpStatus.CREATED).body(MovieResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request){
        
        Movie updated = movieService.updateMovie(id, request);
       
        return ResponseEntity.ok(MovieResponse.from(updated));
    }
}
