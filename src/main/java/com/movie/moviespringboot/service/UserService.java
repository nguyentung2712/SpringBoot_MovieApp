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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public User changeInfo(Integer id, UpsertUserRequest request) {
        User user = getUserById(id);

        if(request.getName().isEmpty()){
            throw new BadRequestException("Name can not be blank");
        }

        if (user.getAvatar().equals(StringUtils.generateLinkImage(user.getName())) || user.getAvatar().isEmpty()) {
            user.setAvatar(StringUtils.generateLinkImage(request.getName()));
        }

        user.setName(request.getName());

        userRepository.save(user);

        return user;
    }

    public String uploadAvatar(Integer id, MultipartFile file) {
        User user = getUserById(id);

        // Upload file into
        String filePath = FileService.uploadFile(file);

        // Update path avatar for user
        user.setAvatar(filePath);
        userRepository.save(user);

        return filePath;
    }

    public void deleteAvatar(Integer id) {
        User user = getUserById(id);

        // Check condition: user's avatar as a default => throw exception
        if(user.getAvatar().equals(StringUtils.generateLinkImage(user.getName()))) {
            throw new RuntimeException("Can not delete avatar");
        }

        FileService.deleteFile(user.getAvatar());
        user.setAvatar(StringUtils.generateLinkImage(user.getName()));
        userRepository.save(user);
    }
}
