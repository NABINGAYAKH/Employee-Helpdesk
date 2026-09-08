package com.nabin.employee_helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<FieldError> fieldErrors= ex.getBindingResult().getFieldErrors();

        Map<String, String> errors = new HashMap<>();

        for(FieldError error : fieldErrors){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException( HttpMessageNotReadableException ex){
        System.out.println(ex.getCause());
        return new ResponseEntity<>(
                new ErrorResponse("Invalid request"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException ex) {

        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

}
