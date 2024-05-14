package com.movie.moviespringboot.model.request;

import com.movie.moviespringboot.model.enums.Enabled;
import com.movie.moviespringboot.model.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertUserRequest {
    String name;
    Date birthday;
    Gender gender;
    String phoneNumber;
    String email;
    String currentPassword;
    String newPassword;
    String confirmNewPassword;
    Enabled enabled;
}
