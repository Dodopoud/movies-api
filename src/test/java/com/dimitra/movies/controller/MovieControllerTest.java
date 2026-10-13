package com.dimitra.movies.controller;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.exception.MovieNotFoundException;
import com.dimitra.movies.service.MovieService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.dimitra.movies.exception.MovieNotFoundException;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

//web part of the application, only for MovieController
//MovieController,GlobalExceptionHandler,validation(@Valid), JSON
@WebMvcTest(MovieController.class)
public class MovieControllerTest {
    @Autowired 
    //MockMvc = fake Postman
    private MockMvc mockMvc;

    @MockitoBean 
    //Fake MovieService 
    private MovieService movieService;

    @Test
    void getMovieById_whenMovieExists_returns200() throws Exception {
    
    //Given
    Movie movie = new Movie("Titanic", "Drama", 7.9, 1997);
    when(movieService.findById(1L)).thenReturn(movie);
    
    //When + Then
    //Test if 200 status is ok and if the title is correct
    mockMvc.perform(get("/movies/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Titanic"));
    }

    @Test 
    void getMovieById_whenMovieNotExists_returns404() throws Exception{
    
    //Given
    when(movieService.findById(999L)).thenThrow(new MovieNotFoundException(999L));
    
    //When + Then
    mockMvc.perform(get("/movies/999"))
            .andExpect(status().isNotFound());
    }

    @Test 
    void deleteMovieById_whenMovieExists_returns204() throws Exception{

    //When + Then
    mockMvc.perform(delete("/movies/1"))
            .andExpect(status().isNoContent());
    verify(movieService).deleteMovieById(1L);
    }

}
