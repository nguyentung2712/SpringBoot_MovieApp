package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.exception.BadRequestException;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.model.enums.UserRole;
import com.movie.moviespringboot.model.request.LoginRequest;
import com.movie.moviespringboot.model.request.RegisterRequest;
import com.movie.moviespringboot.repository.UserRepository;
import com.movie.moviespringboot.utils.StringUtils;
import com.movie.moviespringboot.utils.Validate;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
// Handle login, register and logout using session
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HttpSession session;

    public void login(LoginRequest request) {
        // Find user by email
        // If not throw exception
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email or password is incorrect"));

        // Check request's password match with database's password
        // If not throw exception
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Email or password is incorrect");
        }

        // Save info in session
        session.setAttribute("currentUser", user);
    }

    // Register
    public void register(RegisterRequest request) {
        // Check email is existed or not
        // If existed throw exception
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already existed");
        }

        // Check password match with confirm password
        // If not throw exception
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password not match");
        }

        if(!Validate.ValidateEmail(request.getEmail())){
            throw new BadRequestException("Email incorrect form! Example: user@gmail.com.vn, user@gmail.com, ...");
        }

        if(!Validate.ValidatePassword(request.getPassword())){
            throw new BadRequestException("Password incorrect form! Password need to contain at least 7 to 15 characters, 1 uppercase characters and 1 special character on this list: . , _ ; - @");
        }

        // Encoder password
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        // Create user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(UserRole.USER)
                .avatar(StringUtils.generateLinkImage(request.getName()))
                .build();

        // Save user in database
        userRepository.save(user);
    }

    public void logout() {
        // 1. Delete information
        // session.removeAttribute("currentUser");

        // 2. Set currentUser to null
        session.setAttribute("currentUser", null);
    }
}
