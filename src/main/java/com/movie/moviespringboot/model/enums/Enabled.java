package com.movie.moviespringboot.model.enums;

import lombok.Getter;

@Getter
public enum Enabled {
    ENABLED("Enabled"),
    DISABLED("Disabled");

    private final String value;

    Enabled(String value) {
        this.value = value;
    }
}
