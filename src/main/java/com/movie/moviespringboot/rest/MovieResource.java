package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.Movie;
import com.movie.moviespringboot.model.request.UpsertMovieRequest;
import com.movie.moviespringboot.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/admin/movies")
@RequiredArgsConstructor
public class MovieResource {
    @Autowired
    private final MovieService movieService;

    // create new movie - POST
    @PostMapping("/create-movie")
    public ResponseEntity createMovie(@RequestBody UpsertMovieRequest request) {
        Movie movie = movieService.createMovie(request);
        return ResponseEntity.ok(movie); // status code 200
    }

    // update movie - PUT
    @PutMapping("/{id}/update-movie")
    public ResponseEntity updateMovie(@RequestBody UpsertMovieRequest request, @PathVariable Integer id) {
        Movie movie = movieService.updateMovie(id, request);
        return ResponseEntity.ok(movie); // status code 200
    }

    // delete movie - DELETE
    @DeleteMapping("/{id}/delete-movie")
    public ResponseEntity deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build(); // status code 204
    }

    // upload poster - POST
    @PostMapping("/{id}/upload-poster")
    public ResponseEntity uploadPoster(@RequestParam("file")MultipartFile file, @PathVariable Integer id) {
        String filePath = movieService.uploadPoster(id,file);
        return ResponseEntity.ok(filePath); // status code 200
    }

    // delete poster - DELETE
    @DeleteMapping("/{id}/delete-poster")
    public ResponseEntity deletePoster(@PathVariable Integer id) {
        movieService.deletePoster(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
