package com.dimitra.movies.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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
}