package com.dimitra.movies.service;
import org.springframework.stereotype.Service;
import com.dimitra.movies.repository.ReviewRepository;
import com.dimitra.movies.entity.Review;
import com.dimitra.movies.entity.Movie;
import com.dimitra.movies.dto.ReviewRequest;
import com.dimitra.movies.exception.ReviewNotFoundException;
import java.util.List;

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
}

