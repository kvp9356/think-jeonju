package com.kvp.thinkjeonju.exception.spot;

public class CategoryNotFoundException extends RuntimeException {
	public CategoryNotFoundException(){
        super("Category not found");
    }
    public CategoryNotFoundException(String msg) {
        super(msg);
    }
}
