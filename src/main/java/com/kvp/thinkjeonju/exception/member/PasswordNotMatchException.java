package com.kvp.thinkjeonju.exception.member;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException() {
        super("password not match");
    }

    public PasswordNotMatchException(String msg) {
        super(msg);
    }
}
