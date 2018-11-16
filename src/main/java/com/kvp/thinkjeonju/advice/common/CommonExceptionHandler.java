package com.kvp.thinkjeonju.advice.common;

import com.kvp.thinkjeonju.exception.common.UnAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(UnAuthenticatedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String loginUserNotFound(UnAuthenticatedException exception) {
        log.error("[UnAuthenticatedException] {}", exception.getMessage());
        return "/loginError";
    }
}
