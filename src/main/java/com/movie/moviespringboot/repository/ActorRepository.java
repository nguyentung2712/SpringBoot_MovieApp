package com.movie.moviespringboot.repository;

import com.movie.moviespringboot.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor,Integer> {
}
