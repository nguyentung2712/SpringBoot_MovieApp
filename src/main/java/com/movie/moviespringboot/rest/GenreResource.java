package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.Genre;
import com.movie.moviespringboot.model.request.UpsertGenreRequest;
import com.movie.moviespringboot.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/genres")
@RequiredArgsConstructor
public class GenreResource {
    private final GenreService genreService;

    // Create genre - POST
    @PostMapping("/create-genre")
    public ResponseEntity createGenre(@RequestBody UpsertGenreRequest request) {
        Genre genre = genreService.createGenre(request);
        return ResponseEntity.ok(genre); // status code 200
    }

    // Update genre - PUT
    @PutMapping("/{id}/update-genre")
    public ResponseEntity updateGenre(@RequestBody UpsertGenreRequest request, @PathVariable Integer id) {
        Genre genre = genreService.updateGenre(request,id);
        return ResponseEntity.ok(genre); // status code 200
    }

    // Delete genre
    @DeleteMapping("/{id}/delete-genre")
    public ResponseEntity deleteGenre(@PathVariable Integer id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
