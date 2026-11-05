package com.dimitra.movies.dto;

import com.dimitra.movies.entity.Review;
import java.time.LocalDateTime;

public record ReviewResponse(Long id, 
                            int rating, 
                            String comment, 
                            LocalDateTime createdAt,
                            Long movieId   ) {
    public static ReviewResponse from(Review review){
        return new ReviewResponse(review.getId(), 
                                review.getRating(),
                                review.getComment(),
                                review.getCreatedAt(),
                                review.getMovie().getId());
    }
    
}
