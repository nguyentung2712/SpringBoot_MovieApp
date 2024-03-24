package com.movie.moviespringboot.model.enums;

import lombok.Getter;

@Getter
public enum MovieType {
    PHIM_LE("Single Movie"),
    PHIM_BO("Series Movie"),
    PHIM_CHIEU_RAP("Theater Movie");

    private final String value;

    MovieType(String value) {
        this.value = value;
    }
}
