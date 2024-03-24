package com.movie.moviespringboot.repository;

import com.movie.moviespringboot.entity.Role;
import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    // Find user by email - Login
    // Optional is class that check condition and make sure to find user with no null exception
    Optional<User> findByEmail(String email);

    // Find list user by role - Test
    List<User> findByRole(UserRole userRole);

    // Find email existed in database - Register
    boolean existsByEmail(String email);
}
