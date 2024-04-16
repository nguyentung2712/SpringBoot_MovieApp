package com.movie.moviespringboot.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class Validate {
    public static boolean ValidateEmail(String email){
        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
                + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.matches(emailPattern, email);
    }

    public static boolean ValidatePassword(String password){
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[.,_;-@])(?=\\S+$).{8,20}$";
        return Pattern.matches(passwordPattern, password);
    }
}
