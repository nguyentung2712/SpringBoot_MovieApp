package com.movie.moviespringboot.repository;

import com.movie.moviespringboot.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

    // Find reviews by movieId and order by createdAt Desc
    List<Review> findByMovieIdOrderByCreatedAtDesc(Integer movieId);
}
