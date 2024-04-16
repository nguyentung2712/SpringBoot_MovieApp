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

    // Get all users
    public List<User> getAllUser() {
        return userRepository.findByRole(UserRole.USER);
    }

    // Get user by id
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find user has id is: "+userId));
    }

    // Change password
    public User changePassword(Integer id, UpsertUserRequest request) {
        // Check condition: user want to change password is existed
        User user = getUserById(id);

        // Check condition: current password match
        if (!BeanConfig.bCryptPasswordEncoder().matches(request.getCurrentPassword(), user.getPassword())){
            throw new BadRequestException("Current Password not match");
        }

        // Check condition: regex new password
        if (!Validate.ValidatePassword(request.getNewPassword())){
            throw new BadRequestException("Password incorrect form! Password need to contain at least 8 to 20 characters, 1 uppercase characters and 1 special character on this list: . , _ ; - @");
        }

        // Check condition: new password and confirm new password match
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

    // Change information
    public User changeInfo(Integer id, UpsertUserRequest request) {
        // Check condition: user want to change info is existed
        User user = getUserById(id);

        // Check condition: user's name can not be blank
        if(request.getName().isEmpty()){
            throw new BadRequestException("Name can not be blank");
        }

        // Check condition: change name => change avatar if avatar as default
        if (user.getAvatar().equals(StringUtils.generateLinkImage(user.getName())) || user.getAvatar().isEmpty()) {
            user.setAvatar(StringUtils.generateLinkImage(request.getName()));
        }

        user.setName(request.getName());

        userRepository.save(user);

        return user;
    }

    // Upload avatar
    public String uploadAvatar(Integer id, MultipartFile file) {
        // Check condition: user want to upload avatar is existed
        User user = getUserById(id);

        // Upload file into
        String filePath = FileService.uploadFile(file);

        // Update path avatar for user
        user.setAvatar(filePath);
        userRepository.save(user);

        return filePath;
    }

    // Delete avatar
    public void deleteAvatar(Integer id) {
        // Check condition: user want to delete avatar is existed
        User user = getUserById(id);

        // Check condition: user's avatar as a default => throw exception
        if(user.getAvatar().equals(StringUtils.generateLinkImage(user.getName()))) {
            throw new RuntimeException("Can not delete default avatar");
        }

        FileService.deleteFile(user.getAvatar());
        user.setAvatar(StringUtils.generateLinkImage(user.getName()));
        userRepository.save(user);
    }
}
