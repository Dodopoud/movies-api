package com.dimitra.movies.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dimitra.movies.dto.MovieRequest;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.exception.MovieNotFoundException;
import com.dimitra.movies.service.MovieService;

//web part of the application, only for MovieController
//MovieController,GlobalExceptionHandler,validation(@Valid), JSON
@WebMvcTest(MovieController.class)
public class MovieControllerTest {
        @Autowired
        // MockMvc = fake Postman
        private MockMvc mockMvc;

        @MockitoBean
        // Fake MovieService
        private MovieService movieService;

        @Test
        void getMovieById_whenMovieExists_returns200() throws Exception {

                // Given
                Movie movie = new Movie("Titanic", "Drama", 7.9, 1997);
                when(movieService.findById(1L)).thenReturn(movie);

                // When + Then
                // Test if 200 status is ok and if the title is correct
                mockMvc.perform(get("/movies/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Titanic"));
        }

        @Test
        void getMovieById_whenMovieNotExists_returns404() throws Exception {

                // Given
                when(movieService.findById(999L)).thenThrow(new MovieNotFoundException(999L));

                // When + Then
                mockMvc.perform(get("/movies/999"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deleteMovieById_whenMovieExists_returns204() throws Exception {

                // When + Then
                mockMvc.perform(delete("/movies/1"))
                                .andExpect(status().isNoContent());
                verify(movieService).deleteMovieById(1L);
        }

        @Test
        void deleteMovieById_whenMovieNotExists_returns404() throws Exception {

                // Given
                doThrow(new MovieNotFoundException(999L)).when(movieService).deleteMovieById(999L);

                // When + Then
                mockMvc.perform(delete("/movies/999"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getMovies_byGenre_returns200() throws Exception {

                // Given
                Movie movie = new Movie("Titanic", "Drama", 7.9, 1997);
                when(movieService.search("Drama", null)).thenReturn(List.of(movie));

                // When + Then
                mockMvc.perform(get("/movies?genre=Drama"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1)) // how many elements in the list
                                .andExpect(jsonPath("$[0].title").value("Titanic")); // the title of the first element
        }

        @Test
        void createMovie_withValidRequest_returns201() throws Exception {
                // Given
                Movie movie = new Movie("Titanic", "Drama", 7.9, 1997);
                when(movieService.createMovie(any(MovieRequest.class))).thenReturn(movie);

                // When + Then
                mockMvc.perform(post("/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "title": "Titanic",
                                                  "genre": "Drama",
                                                  "rating": 7.9,
                                                  "releaseYear": 1997
                                                }

                                                 """))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.title").value("Titanic"));
        }

        @Test
        void createMovie_withInvalidRequest_returns400() throws Exception {
                // When + then
                mockMvc.perform(post("/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                      "title": "",
                                                      "genre": "Drama",
                                                      "rating": 15,
                                                      "releaseYear": 1997
                                                }
                                                 """))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.title").exists())
                                .andExpect(jsonPath("$.rating").exists());
        }

        @Test
        void updateMovie_whenMovieExists_returns200() throws Exception {
                // Given
                Movie updated = new Movie("Titanic", "Drama", 7.9, 1997);
                when(movieService.updateMovie(eq(1L), any(MovieRequest.class))).thenReturn(updated);

                // When + Then
                mockMvc.perform(put("/movies/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                "title": "Titanic",
                                                "genre": "Drama",
                                                "rating": 7.9,
                                                "releaseYear": 1997
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.genre").value("Drama"))
                                .andExpect(jsonPath("$.rating").value(7.9));
        }

        @Test
        void updateMovie_whenMovieNotExists_returns404() throws Exception {
                // Given
                when(movieService.updateMovie(eq(999L), any(MovieRequest.class)))
                                .thenThrow(new MovieNotFoundException(999L));

                // When + Then
                mockMvc.perform(put("/movies/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                 "title": "Titanic",
                                                "genre": "Drama",
                                                "rating": 7.9,
                                                "releaseYear": 1997
                                                }
                                                """))
                                .andExpect(status().isNotFound());
        }

}
