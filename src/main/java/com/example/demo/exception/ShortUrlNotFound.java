package com.example.demo.exception;

public class ShortUrlNotFound extends  RuntimeException {

    public ShortUrlNotFound(String err) {
        super(err);
    }
}
