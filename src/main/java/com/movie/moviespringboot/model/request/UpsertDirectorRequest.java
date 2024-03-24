package com.movie.moviespringboot.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertDirectorRequest {
    String name;
    Date birthday;
    String description;
}
