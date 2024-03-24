package com.movie.moviespringboot.exception;

import com.movie.moviespringboot.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Class handle exception
@ControllerAdvice
public class CustomExceptionHandler{

    // Handle Bad Request Exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestException(BadRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    // Handle Not Found Result Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND); // 404
    }

    // Handle All Exception Remaining
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }
}
