package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.model.request.UpsertUserRequest;
import com.movie.moviespringboot.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserResource {
    @Autowired
    private final UserService userService;

    @PostMapping("/{id}/change-password")
    public ResponseEntity changePassword(@PathVariable Integer id, @RequestBody UpsertUserRequest request) {
        User user = userService.changePassword(id,request);
        return ResponseEntity.ok(user); // status code 200
    }
}
