package com.movie.moviespringboot.repository;

import com.movie.moviespringboot.entity.Episode;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

    // Find episode by movieId and order by displayOrderAsc
    List<Episode> findByMovie_IdOrderByDisplayOrderAsc(Integer movieId);

    // Find episode by movieId and status and order by displayOrderAsc
    List<Episode> findByMovie_IdAndStatusOrderByDisplayOrderAsc(Integer movieId, Boolean status);

    // Find episode by movieId and order by displayOrderDesc
    List<Episode> findByMovie_Id(Integer movieId);

    // <Optional> Find episode by movieId and status and order by displayOrder
    Optional<Episode> findByMovie_IdAndDisplayOrderAndStatus(Integer movieId, Integer displayOrder, Boolean status);
}
