package edu.springframework.spring6customerassignment.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler
    ResponseEntity handleJpaViolation(TransactionSystemException exception) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception.getCause().getCause();
        List errorList =
            constraintViolationException.getConstraintViolations().stream().
            map(constraintError -> {
               HashMap<String, String> errorMap = new HashMap<>();
               errorMap.put(constraintError.getPropertyPath().toString(), constraintError.getMessage());
               return errorMap;
            }).toList();

        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleErrors(MethodArgumentNotValidException exception) {
        List errorList =
            exception.getFieldErrors().stream().
                map(fieldError -> {
                    HashMap<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).toList();

        return ResponseEntity.badRequest().body(errorList);
    }

}
