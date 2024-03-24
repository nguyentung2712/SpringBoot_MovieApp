package com.movie.moviespringboot.repository;

import com.movie.moviespringboot.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre,Integer> {
}
