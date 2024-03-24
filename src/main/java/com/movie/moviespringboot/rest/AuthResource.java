package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.model.request.LoginRequest;
import com.movie.moviespringboot.model.request.RegisterRequest;
import com.movie.moviespringboot.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// API of login/register
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthResource {
    // 1. Client will send request has information to server
    // 2. Server will read information from request -> handle information -> send result to Client
    @Autowired
    private final AuthService authService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request){
        authService.login(request);
        return ResponseEntity.ok().build();
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    // LOG OUT
    @PostMapping("/logout")
    public ResponseEntity logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
}
