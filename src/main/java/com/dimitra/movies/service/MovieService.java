package com.dimitra.movies.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dimitra.movies.dto.MovieRequest;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.repository.MovieRepository;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    //constructor injection
    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public Movie findById(Long id){
        return  movieRepository.findById(id).orElse(null);
        
    }

    public List<Movie> findAll(){
        return movieRepository.findAll();
    }

    public List<Movie> search (String genre, Double minRating){
        if (genre != null && minRating != null){
            return movieRepository.findByGenreIgnoreCaseAndRatingGreaterThanEqual(genre, minRating);
        }
        else if (genre != null){
            return movieRepository.findByGenreIgnoreCase(genre);
        }
        else if(minRating != null){
            return movieRepository.findByRatingGreaterThanEqual(minRating);
        }
        else{
            return movieRepository.findAll();
        }
    }

    public boolean deleteMovieById(Long id){
        if(!movieRepository.existsById(id)){
            return false;
        }
        movieRepository.deleteById(id);
        return true;
    }

    public Movie createMovie(MovieRequest request){
       Movie movie = new Movie(request.title(),
                                request.genre(), 
                                request.rating(), 
                                request.releaseYear());

       return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, MovieRequest request){
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null){
            return null;
        }

        movie.setTitle(request.title());
        movie.setGenre(request.genre());
        movie.setRating(request.rating());
        movie.setReleaseYear(request.releaseYear());

        return movieRepository.save(movie);
    }

}