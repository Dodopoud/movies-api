package com.dimitra.movies.controller;

import com.dimitra.movies.service.ReviewService;
import com.dimitra.movies.entity.Review;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.dto.ReviewResponse;
import com.dimitra.movies.dto.ReviewRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long movieId){
        List<ReviewResponse> reviews = reviewService.findByMovieId(movieId) 
                                                    .stream()
                                                    .map(ReviewResponse::from)
                                                    .toList();
        return ResponseEntity.ok(reviews);

    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@PathVariable Long movieId, @Valid @RequestBody ReviewRequest request){
        Review saved = reviewService.createReview(movieId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewResponse.from(saved));
    }


    

}
