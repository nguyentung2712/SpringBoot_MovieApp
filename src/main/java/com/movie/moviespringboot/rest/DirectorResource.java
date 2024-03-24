package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.Director;
import com.movie.moviespringboot.model.request.UpsertDirectorRequest;
import com.movie.moviespringboot.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/admin/directors")
@RequiredArgsConstructor
public class DirectorResource {
    @Autowired
    private final DirectorService directorService;

    // create director - POST
    @PostMapping("/create-director")
    public ResponseEntity createDirector(@RequestBody UpsertDirectorRequest request) {
        Director director = directorService.createDirector(request);
        return ResponseEntity.ok(director); // status code 200
    }

    // update director - PUT
    @PutMapping("/{id}/update-director")
    public ResponseEntity updateDirector(@RequestBody UpsertDirectorRequest request, @PathVariable Integer id) {
        Director director = directorService.updateDirector(id, request);
        return ResponseEntity.ok(director); // status code 200
    }

    // delete director - DELETE
    @DeleteMapping("/{id}/delete-director")
    public ResponseEntity deleteDirector(@PathVariable Integer id) {
        directorService.deleteDirector(id);
        return ResponseEntity.noContent().build(); // status code 204
    }

    // upload avatar - POST
    @PostMapping("/{id}/upload-avatar")
    public ResponseEntity uploadAvatar(@RequestParam("file")MultipartFile file, @PathVariable Integer id) {
        String filePath = directorService.uploadAvatar(id, file);
        return ResponseEntity.ok(filePath); // status code 200
    }

    // delete avatar - DELETE
    @DeleteMapping("/{id}/delete-avatar")
    public ResponseEntity deleteAvatar(@PathVariable Integer id) {
        directorService.deleteAvatar(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
