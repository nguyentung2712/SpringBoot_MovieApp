package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Movie;
import com.movie.moviespringboot.entity.Review;
import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.exception.BadRequestException;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.model.enums.UserRole;
import com.movie.moviespringboot.repository.MovieRepository;
import com.movie.moviespringboot.repository.ReviewRepository;
import com.movie.moviespringboot.repository.UserRepository;
import com.movie.moviespringboot.model.request.UpsertReviewRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    public final UserRepository userRepository;
    public final MovieRepository movieRepository;
    public final ReviewRepository reviewRepository;
    public final HttpSession httpSession;

    // get list review by movie id
    public List<Review> getReviewOfMovie(Integer movieId) {
        return reviewRepository.findByMovieIdOrderByCreatedAtDesc(movieId);
    }

    // create review
    public Review createReview(UpsertReviewRequest request) {
        User user = (User) httpSession.getAttribute("currentUser");

        Movie movie = movieRepository.findById(request.getMovieId()) // check movie existed or not
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        // create review
        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .movie(movie)
                .user(user)
                .build();

        return reviewRepository.save(review);
    }

    // update review
    public Review updateReview(Integer id, UpsertReviewRequest request) {
        User user = (User) httpSession.getAttribute("currentUser");

        Movie movie = movieRepository.findById(request.getMovieId()) // check movie existed or not
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Review review = reviewRepository.findById(id) // check review existed or not
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        // Check user is who create review or not
        if(!review.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not the owner of this review");
        }

        // Check review belong to movie or not
        if(!review.getMovie().getId().equals(movie.getId())) {
            throw new BadRequestException("This review is not belong to this movie");
        }

        // Update review
        review.setContent(request.getContent());
        review.setRating(request.getRating());

        return reviewRepository.save(review);
    }

    // delete review
    // TODO: change condition to make admin can delete any comment of user
    public void deleteReview(Integer id) {
        User user = (User) httpSession.getAttribute("currentUser");

        Review review = reviewRepository.findById(id) // check review existed or not
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        // Check condition: Only review's owner or admin have permission to delete review
        if (review.getUser().getId().equals(user.getId()) || userRepository.findAll().stream().anyMatch(userMatch -> userMatch.getRole().equals(UserRole.ADMIN))) {
            // Delete Review
            reviewRepository.delete(review);
        } else {
            throw new BadRequestException("You are not allow to delete this review");
        }
    }
}
