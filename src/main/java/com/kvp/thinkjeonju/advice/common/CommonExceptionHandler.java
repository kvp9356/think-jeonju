package com.kvp.thinkjeonju.advice.common;

import com.kvp.thinkjeonju.exception.common.UnAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.error("[MethodArgumentNotValidException] {}", exception.getMessage());
        return new ResponseEntity(exception.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<String> loginUserNotFound(UnAuthenticatedException exception) {
        log.error("[UnAuthenticatedException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
