package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.Review;
import com.movie.moviespringboot.model.request.UpsertReviewRequest;
import com.movie.moviespringboot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewResource {
    @Autowired
    private final ReviewService reviewService;

    // create review - POST
    @PostMapping("/create-review")
    public ResponseEntity createReview(@RequestBody UpsertReviewRequest request) {
        Review review = reviewService.createReview(request);
        return new ResponseEntity(review, HttpStatus.CREATED); // status code 201
    }

    // update review - PUT
    @PutMapping("/{id}/update-review")
    public ResponseEntity updateReview(@RequestBody UpsertReviewRequest request, @PathVariable Integer id) {
        Review review = reviewService.updateReview(id, request);
        return ResponseEntity.ok(review); // status code 200
    }

    // delete review - DELETE
    @DeleteMapping("/{id}/delete-review")
    public ResponseEntity deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build(); // status code 204
    }

}
