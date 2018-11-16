package com.kvp.thinkjeonju.exception.common;

public class UnAuthenticatedException extends RuntimeException{
    public UnAuthenticatedException() {
        super("unAuthenticated exception");
    }

    public UnAuthenticatedException(String msg) {
        super(msg);
    }
}
