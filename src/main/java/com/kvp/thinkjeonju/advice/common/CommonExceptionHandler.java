package com.kvp.thinkjeonju.advice.common;

import com.kvp.thinkjeonju.exception.common.UnAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(UnAuthenticatedException.class)
    public String loginUserNotFound(UnAuthenticatedException exception) {
        log.error("[UnAuthenticatedException] {}", exception.getMessage());
        return "/error";
    }
}
