package com.dimitra.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dimitra.movies.entity.Review;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByMovieId(Long movieId);
    
}
