package com.dimitra.movies.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import com.dimitra.movies.dto.MovieRequest;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.exception.MovieNotFoundException;
import com.dimitra.movies.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    //create fake repository
    @Mock
    private MovieRepository movieRepository;

    //create a real MovieService with the fake repository
    @InjectMocks
    private MovieService movieService;


    @Test
    void findById_whenMovieExists_returnsMovie(){
        //Given
        Movie movie = new Movie("Titanic", "Drama", 7.9, 1997);
        when (movieRepository.findById(1L)).thenReturn(Optional.of(movie));


        //When
        Movie result = movieService.findById(1L);

        //Then
        assertThat(result.getTitle()).isEqualTo("Titanic");
    }

    @Test
    void findById_whenMovieNotExists_throwsException(){
        //Given
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        //When + Then
        assertThatThrownBy(() -> movieService.findById(999L))
                                             .isInstanceOf(MovieNotFoundException.class);

    }

    @Test
    void deleteMovieById_whenMovieExists_deletesMovie(){
        //Given
        Movie movie = new Movie("Titanic", "Drama", 7.9, 1997);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        //When
        movieService.deleteMovieById(1L);

        //Then
        verify(movieRepository).delete(movie);
    }

    @Test 
    void deleteMovieById_whenMovieNotExists_throwsException(){
        //Given
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        //When + Then
        assertThatThrownBy(() -> movieService.deleteMovieById(999L))
                                              .isInstanceOf(MovieNotFoundException.class);
    }

    @Test
    void updateMovie_whenMovieExists_updatesMovie(){

        //Given
        Movie movie = new Movie("Titanic", "Romance", 7.0, 1997);              // old
        MovieRequest request = new MovieRequest("Titanic", "Drama", 7.9, 1997); // new
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        //When
        Movie result = movieService.updateMovie(1L, request);

        //Then
        assertThat(result.getGenre()).isEqualTo("Drama");
        assertThat(result.getRating()).isEqualTo(7.9);
    }


    @Test
    void updateMovie_whenMovieNotExists_throwsException(){
        //Given
        MovieRequest request = new MovieRequest("Titanic", "Drama", 7.9, 1997);
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());        
        
        //When + Then
        assertThatThrownBy(() -> movieService.updateMovie(999L, request))
                                             .isInstanceOf(MovieNotFoundException.class);
    }

    @Test
    void createMovie_withValidRequest_returnsSavedMovie(){
        //Given
        MovieRequest movieRequest = new MovieRequest("Spirited Away", "Animation", 8.6,2001);
        when (movieRepository.save(any(Movie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //When
        Movie result = movieService.createMovie(movieRequest);

        //Then
        assertThat(result.getTitle()).isEqualTo("Spirited Away");
        assertThat(result.getGenre()).isEqualTo("Animation");
        assertThat(result.getRating()).isEqualTo(8.6);
        assertThat(result.getReleaseYear()).isEqualTo(2001);
    }

        @Test
    void search_withGenreAndMinRating_returnsFilteredMovies(){
        //Given
        Movie movie = new Movie("The Godfather", "Drama", 9.2, 1972);
        List<Movie> movies = List.of(movie);
        when(movieRepository.findByGenreIgnoreCaseAndRatingGreaterThanEqual("Drama", 8.5)).thenReturn(movies);

        //When
        List<Movie> result = movieService.search("Drama", 8.5);

        //Then
        assertThat(result).isEqualTo(movies);
    }

    @Test
    void search_withGenreOnly_returnsMoviesByGenre(){
        //Given
        Movie movie = new Movie("Titanic", "Drama", 7.9, 1997);
        List<Movie> movies = List.of(movie);
        when(movieRepository.findByGenreIgnoreCase("Drama")).thenReturn(movies);

        //When
        List<Movie> result = movieService.search("Drama", null);

        //Then
        assertThat(result).isEqualTo(movies);
    }

    @Test
    void search_withMinRatingOnly_returnsMoviesByRating(){
        //Given
        Movie movie = new Movie("Spirited Away", "Animation", 8.6, 2001);
        List<Movie> movies = List.of(movie);
        when(movieRepository.findByRatingGreaterThanEqual(8.5)).thenReturn(movies);

        //When
        List<Movie> result = movieService.search(null, 8.5);

        //Then
        assertThat(result).isEqualTo(movies);
    }

    @Test
    void search_withNoFilters_returnsAllMovies(){
        //Given
        Movie movie = new Movie("Toy Story", "Animation", 8.3, 1995);
        List<Movie> movies = List.of(movie);
        when(movieRepository.findAll()).thenReturn(movies);

        //When
        List<Movie> result = movieService.search(null, null);

        //Then
        assertThat(result).isEqualTo(movies);
    }

}