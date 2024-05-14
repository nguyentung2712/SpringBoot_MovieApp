package com.movie.moviespringboot.entity;

import com.movie.moviespringboot.model.enums.Enabled;
import com.movie.moviespringboot.model.enums.Gender;
import com.movie.moviespringboot.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increase id
    Integer id;

    @Column(nullable = false)
    String name;

    @Column
    Date birthday;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column
    String phoneNumber;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column
    String avatar;

    @Enumerated(EnumType.STRING)
    UserRole role;

    // Manage enabled by ROLE_ADMIN
    @Enumerated(EnumType.STRING)
    Enabled enabled;
}
