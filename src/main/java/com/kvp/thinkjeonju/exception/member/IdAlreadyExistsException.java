package com.kvp.thinkjeonju.exception.member;

public class IdAlreadyExistsException extends RuntimeException {
    public IdAlreadyExistsException() {
        super("Id Already Exists");
    }

    public IdAlreadyExistsException(String msg) {
        super(msg);
    }
}
