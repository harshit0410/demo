package com.example.demo.exception;

public class ShortUrlExists extends  RuntimeException {

    public  ShortUrlExists (String message) {
        super(message);
    }
}
