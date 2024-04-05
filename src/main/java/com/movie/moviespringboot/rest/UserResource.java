package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.model.request.UpsertUserRequest;
import com.movie.moviespringboot.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserResource {
    @Autowired
    private final UserService userService;

    // Change user's password - PUT
    @PutMapping("/{id}/change-password")
    public ResponseEntity changePassword(@PathVariable Integer id, @RequestBody UpsertUserRequest request) {
        User user = userService.changePassword(id,request);
        return ResponseEntity.ok(user); // status code 200
    }

    // Change user's info - POST
    @PutMapping("/{id}/change-info")
    public ResponseEntity changeInfo(@PathVariable Integer id, @RequestBody UpsertUserRequest request) {
        User user = userService.changeInfo(id,request);
        return ResponseEntity.ok(user); // status code 200
    }

    // Upload user's avatar - POST
    @PostMapping("/{id}/upload-avatar")
    public ResponseEntity uploadAvatar(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        String filePath = userService.uploadAvatar(id,file);
        return ResponseEntity.ok(filePath); // status code 200
    }

    // Delete user's avatar - DELETE
    @DeleteMapping("/{id}/delete-avatar")
    public ResponseEntity deleteAvatar(@PathVariable Integer id) {
        userService.deleteAvatar(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
