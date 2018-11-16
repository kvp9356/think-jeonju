package com.kvp.thinkjeonju.exception.common;

public class DataBaseException extends RuntimeException {
    public DataBaseException() {
        super("Database Exception");
    }

    public DataBaseException(String msg) {
        super(msg);
    }
}