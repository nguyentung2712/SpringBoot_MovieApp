package com.movie.moviespringboot.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertUserRequest {
    String name;
    String email;
    String currentPassword;
    String newPassword;
    String confirmNewPassword;
}
