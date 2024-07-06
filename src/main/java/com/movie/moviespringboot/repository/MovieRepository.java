package com.movie.moviespringboot.repository;

import com.movie.moviespringboot.entity.Movie;
import com.movie.moviespringboot.model.enums.MovieType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Movie: object
// Integer: value type
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    // Find movie by type and status and order by "sort"
    List<Movie> findByTypeAndStatus(MovieType type, boolean status, Sort sort);

    // Find movie by id
    Movie findMovieByIdAndSlugAndStatus(int id, String slug, boolean status);

    // Find Page Movie by type and status
    Page<Movie> findByTypeAndStatus(MovieType type, boolean status, Pageable pageable);

    // Find Page Movie by status
    Page<Movie> findByStatus(boolean status, Pageable pageable);

    // Get release movie in movie detail page
    Page<Movie> getMovieByType(MovieType type, boolean status, Pageable pageable);

}