package com.kvp.thinkjeonju.advice.member;

import com.kvp.thinkjeonju.exception.member.IdAlreadyExistsException;
import com.kvp.thinkjeonju.exception.member.PasswordNotMatchException;
import com.kvp.thinkjeonju.exception.member.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiMemberExceptionHandler {

    @ExceptionHandler(IdAlreadyExistsException.class)
    public ResponseEntity<String> idAlreadyExists(IdAlreadyExistsException exception) {
        log.error("[IdAlreadyExistsException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound(UserNotFoundException exception) {
        log.error("[UserNotFoundException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<String> passwordNotMatch(PasswordNotMatchException exception) {
        log.error("[PasswordNotMatchException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
