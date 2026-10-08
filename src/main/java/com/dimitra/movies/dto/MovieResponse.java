package com.dimitra.movies.dto;

import com.dimitra.movies.entity.Movie;

public record MovieResponse(Long id, String title, String genre, double rating, int releaseYear) {
    public static MovieResponse from (Movie movie){
        return new MovieResponse(movie.getId(), 
                                movie.getTitle(), 
                                movie.getGenre(), 
                                movie.getRating(), 
                                movie.getReleaseYear());
    }
}
