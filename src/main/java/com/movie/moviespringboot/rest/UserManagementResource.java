package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.model.request.UpsertUserRequest;
import com.movie.moviespringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserManagementResource {
    @Autowired
    UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity createUser(@RequestBody UpsertUserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(user); // status code 200
    }

    @PutMapping("/{id}/change-info-user")
    public ResponseEntity changeInfoUser(@PathVariable Integer id, @RequestBody UpsertUserRequest request) {
        userService.changeInfoUserByAdmin(id,request);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
