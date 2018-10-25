package com.kvp.thinkjeonju.advice.member;

import com.kvp.thinkjeonju.exception.member.IdAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(IdAlreadyExistsException.class)
    public ResponseEntity<String> idAlreadyExists(IdAlreadyExistsException exception) {
        log.error("[IdAlreadyExistsException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
