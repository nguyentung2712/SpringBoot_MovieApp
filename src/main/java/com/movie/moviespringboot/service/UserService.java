package com.movie.moviespringboot.service;

import com.movie.moviespringboot.config.BeanConfig;
import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.exception.BadRequestException;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.model.enums.UserRole;
import com.movie.moviespringboot.model.request.UpsertUserRequest;
import com.movie.moviespringboot.repository.UserRepository;
import com.movie.moviespringboot.utils.StringUtils;
import com.movie.moviespringboot.utils.Validate;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUser() {
        return userRepository.findByRole(UserRole.USER);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find user has id is: "+userId));
    }

    public User changePassword(Integer id, UpsertUserRequest request) {
        // Find user by id
        User user = getUserById(id);

        if (!BeanConfig.bCryptPasswordEncoder().matches(request.getCurrentPassword(), user.getPassword())){
            throw new BadRequestException("Password not match");
        }

        if (!Validate.ValidatePassword(request.getNewPassword())){
            throw new BadRequestException("Password incorrect form! Password need to contain at least 7 to 15 characters, 1 uppercase characters and 1 special character on this list: . , _ ; - @");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new BadRequestException("Password and Confirm Password not match");
        }

        // Encoder New Password
        String encoderPassword = BeanConfig.bCryptPasswordEncoder().encode(request.getNewPassword());

        // Change password
        user.setPassword(encoderPassword);

        // Save new user's information to database
        userRepository.save(user);

        return user;
    }
}
