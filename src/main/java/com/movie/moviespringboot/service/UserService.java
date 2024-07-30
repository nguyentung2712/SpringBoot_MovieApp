package com.movie.moviespringboot.service;

import com.movie.moviespringboot.config.BeanConfig;
import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.exception.BadRequestException;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.model.enums.Enabled;
import com.movie.moviespringboot.model.enums.UserRole;
import com.movie.moviespringboot.model.request.UpsertUserRequest;
import com.movie.moviespringboot.repository.UserRepository;
import com.movie.moviespringboot.utils.StringUtils;
import com.movie.moviespringboot.utils.Validate;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final HttpSession session;

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
        if(request.getName().isEmpty()) {
            throw new BadRequestException("Name can not be blank");
        }
        // Check condition: user's phone number can not be blank
        if (request.getPhoneNumber().isEmpty()) {
            throw new BadRequestException("Phone number can not be blank");
        }

        // Check condition: phone number validate
        if (!Validate.ValidatePhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number incorrect form! Phone number start with 0 and must be 10 digits including 0 in the count");
        }

        // Check condition: birthday reasonable
        if (request.getBirthday().getYear() == new Date().getYear()) {
            if ((request.getBirthday().getMonth() == new Date().getMonth()) && (request.getBirthday().getDate() > new Date().getDate())) {
                throw new BadRequestException("Birthday must be before or on "+ new Date().getDate());
            }
            if (request.getBirthday().getMonth() > new Date().getMonth()) {
                throw new BadRequestException("Birthday month must be before or in "+ (new Date().getMonth() + 1));
            }
        }
        if (request.getBirthday().getYear() > (new Date().getYear())) {
            throw new BadRequestException("Birthday year must be before or in "+ (new Date().getYear() + 1900));
        }

        // Check condition: change name => change avatar if avatar as default
        if (user.getAvatar().equals(StringUtils.generateLinkImage(user.getName())) || user.getAvatar().isEmpty()) {
            user.setAvatar(StringUtils.generateLinkImage(request.getName()));
        }

        // Set entity
        user.setName(request.getName());
        user.setBirthday(request.getBirthday());
        user.setGender(request.getGender());

        // Check condition: phone number existed
        if (userRepository.findAll().stream().noneMatch(userMatch -> userMatch.getPhoneNumber().equals(request.getPhoneNumber()))
                || user.getPhoneNumber().equals(request.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
        } else {
            throw new RuntimeException("Phone number already existed");
        }

        // return user after change info
        User newUser = userRepository.save(user);
        session.setAttribute("currentUser", newUser);

        return newUser;
    }

    // Upload avatar
    public User uploadAvatar(Integer id, MultipartFile file) {
        // Check condition: user want to upload avatar is existed
        User user = getUserById(id);

        // Upload file into
        String filePath = FileService.uploadFile(file);

        // Update path avatar for user
        user.setAvatar(filePath);

        // return user after upload avatar
        User newUser = userRepository.save(user);
        session.setAttribute("currentUser", newUser);

        return newUser;
    }

    // Delete avatar
    public User deleteAvatar(Integer id) {
        // Check condition: user want to delete avatar is existed
        User user = getUserById(id);

        // Check condition: user's avatar as a default => throw exception
        if(user.getAvatar().equals(StringUtils.generateLinkImage(user.getName()))) {
            throw new RuntimeException("Can not delete default avatar");
        }

        FileService.deleteFile(user.getAvatar());
        user.setAvatar(StringUtils.generateLinkImage(user.getName()));

        // return user after delete avatar
        User newUser = userRepository.save(user);
        session.setAttribute("currentUser", newUser);

        return newUser;
    }

    // Create user
    public User createUser(UpsertUserRequest request) {
        // Check condition: user's name can not be blank
        if (request.getName().isEmpty()){
            throw new BadRequestException("Name can not be blank");
        }
        // Check condition: user's phone number can not be blank
        if (request.getPhoneNumber().isEmpty()){
            throw new BadRequestException("Phone number can not be blank");
        }
        // Check condition: user's email can not be blank
        if (request.getEmail().isEmpty()){
            throw new BadRequestException("Email can not be blank");
        }
        // Check condition: user's password can not be blank
        if (request.getNewPassword().isEmpty()){
            throw new BadRequestException("Password can not be blank");
        }

        // Check condition: email existed
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already existed");
        }
        // Check condition: phone number existed
        if (userRepository.findAll().stream().anyMatch(user -> user.getPhoneNumber().equals(request.getPhoneNumber()))) {
            throw new RuntimeException("Phone number already existed");
        }

        // Check condition: email validate
        if (!Validate.ValidateEmail(request.getEmail())) {
            throw new RuntimeException("Email incorrect form! Example: user@gmail.com.vn, user@gmail.com, ...");
        }
        // Check condition: phone number validate
        if (!Validate.ValidatePhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number incorrect form! Phone number start with 0 and must be 10 digits including 0 in the count");
        }
        // Check condition: password validate
        if (!Validate.ValidatePassword(request.getNewPassword())) {
            throw new RuntimeException("Password incorrect form! Password need to contain at least 7 to 15 characters, 1 uppercase characters and 1 special character on this list: . , _ ; - @");
        }

        // Check condition: password and confirm password not match
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BadRequestException("Password and Confirm Password not match");
        }

        // Check condition: birthday reasonable
        if (request.getBirthday().getYear() == new Date().getYear()) {
            if ((request.getBirthday().getMonth() == new Date().getMonth()) && (request.getBirthday().getDate() > new Date().getDate())) {
                throw new BadRequestException("Birthday must be before or on "+ new Date().getDate());
            }
            if (request.getBirthday().getMonth() > new Date().getMonth()) {
                throw new BadRequestException("Birthday month must be before or in "+ (new Date().getMonth() + 1));
            }
        }
        if (request.getBirthday().getYear() > (new Date().getYear())) {
            throw new BadRequestException("Birthday year must be before or in "+ (new Date().getYear() + 1900));
        }

        // Encoder password
        String encodePassword = bCryptPasswordEncoder.encode(request.getNewPassword());

        User user = User.builder()
                .name(request.getName())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(encodePassword)
                .avatar(StringUtils.generateLinkImage(request.getName()))
                .role(UserRole.USER)
                .enabled(Enabled.ENABLED)
                .build();

        // Save user in database
        userRepository.save(user);

        return user;
    }

    // Update info user and disable user's account
    public User changeInfoUserByAdmin(Integer id, UpsertUserRequest request) {
        User user = getUserById(id);

        // Check condition: user's name can not be blank
        if(request.getName().isEmpty()){
            throw new BadRequestException("Name can not be blank");
        }

        // Check condition: user's phone number can not be blank
        if (request.getPhoneNumber().isEmpty()){
            throw new BadRequestException("Phone number can not be blank");
        }
        // Check condition: phone number validate
        if (!Validate.ValidatePhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number incorrect form! Phone number start with 0 and must be 10 digits including 0 in the count");
        }

        // Check condition: birthday reasonable
        if (request.getBirthday().getYear() == new Date().getYear()) {
            if ((request.getBirthday().getMonth() == new Date().getMonth()) && (request.getBirthday().getDate() > new Date().getDate())) {
                throw new BadRequestException("Birthday must be before or on "+ new Date().getDate());
            }
            if (request.getBirthday().getMonth() > new Date().getMonth()) {
                throw new BadRequestException("Birthday month must be before or in "+ (new Date().getMonth() + 1));
            }
        }
        if (request.getBirthday().getYear() > (new Date().getYear())) {
            throw new BadRequestException("Birthday year must be before or in "+ (new Date().getYear() + 1900));
        }

        user.setName(request.getName());
        user.setBirthday(request.getBirthday());
        user.setGender(request.getGender());

        // Check condition: phone number existed
        if (userRepository.findAll().stream().noneMatch(userMatch -> userMatch.getPhoneNumber().equals(request.getPhoneNumber()))
                || user.getPhoneNumber().equals(request.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
        } else {
            throw new RuntimeException("Phone number already existed");
        }

        user.setEnabled(request.getEnabled());

        return userRepository.save(user);
    }
}
