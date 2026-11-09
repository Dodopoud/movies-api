package com.dimitra.movies.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dimitra.movies.dto.ReviewRequest;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.entity.Review;
import com.dimitra.movies.exception.ReviewNotFoundException;
import com.dimitra.movies.repository.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieService movieService;

    //constructor injection
    public ReviewService(ReviewRepository reviewRepository, MovieService movieService){
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
    }

    public Review findById(Long id){
        return reviewRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException(id)); 
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }

    public List<Review> findByMovieId(Long movieId){
        movieService.findById(movieId);
        return reviewRepository.findByMovieId(movieId);

    }

    public Review createReview(Long movieId, ReviewRequest request){
        Movie movie = movieService.findById(movieId);
        Review review = new Review(request.rating(),
                                    request.comment(),
                                    movie );
    return reviewRepository.save(review);
 
    }

    public void deleteReviewById(Long movieId, Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if (!review.getMovie().getId().equals(movieId)) {
            throw new ReviewNotFoundException(reviewId);
        }

        reviewRepository.delete(review);
        
    }

    public Review updateReview(Long movieId, Long reviewId, ReviewRequest request){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if(!review.getMovie().getId().equals(movieId)){
            throw new ReviewNotFoundException(reviewId);
        }
        review.setRating(request.rating());
        review.setComment(request.comment());

        return reviewRepository.save(review);
       
    }
}

