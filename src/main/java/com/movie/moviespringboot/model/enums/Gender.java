package com.movie.moviespringboot.model.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    LGBT("LGBT");

    private final String value;

    Gender(String value) {
        this.value = value;
    }
}
