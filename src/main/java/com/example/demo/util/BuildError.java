package com.example.demo.util;

import com.example.demo.dto.ErrorResponse;

public class BuildError {
    public static ErrorResponse buildError(String message, String code) {
        return ErrorResponse.builder().message(message).code(code).build();
    }
}
