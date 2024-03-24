package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.Actor;
import com.movie.moviespringboot.model.request.UpsertActorRequest;
import com.movie.moviespringboot.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/admin/actors")
@RequiredArgsConstructor
public class ActorResource {
    @Autowired
    private final ActorService actorService;

    // create new actor - POST
    @PostMapping("/create-actor")
    public ResponseEntity createActor(@RequestBody UpsertActorRequest request) {
        Actor actor = actorService.createActor(request);
        return ResponseEntity.ok(actor); // status code 200
    }

    // update actor - PUT
    @PutMapping("/{id}/update-actor")
    public ResponseEntity updateActor(@RequestBody UpsertActorRequest request, @PathVariable Integer id) {
        Actor actor = actorService.updateActor(id, request);
        return ResponseEntity.ok(actor); // status code 200
    }

    // delete actor - DELETE
    @DeleteMapping("/{id}/delete-actor")
    public ResponseEntity deleteActor(@PathVariable Integer id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build(); // status code 204
    }

    // upload avatar - POST
    @PostMapping("/{id}/upload-avatar")
    public ResponseEntity uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
        String filePath = actorService.uploadAvatar(id, file);
        return ResponseEntity.ok(filePath); // status code 200
    }

    // delete avatar - DELETE
    @DeleteMapping("/{id}/delete-avatar")
    public ResponseEntity deleteAvatar(@PathVariable Integer id) {
        actorService.deleteAvatar(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
