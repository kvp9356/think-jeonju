package com.kvp.thinkjeonju.advice.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiCommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.error("[MethodArgumentNotValidException] {}", exception.getMessage());
        return new ResponseEntity(exception.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST);
    }
}
