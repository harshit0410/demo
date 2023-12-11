package com.example.demo.controller;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.LongUrlResponse;
import com.example.demo.dto.ShortUrlRequest;
import com.example.demo.dto.ShortUrlResponse;
import com.example.demo.exception.ShortUrlExists;
import com.example.demo.service.ShortUrlService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShortUrlControllerTest {
    @Mock
    ShortUrlService shortUrlService;

    @InjectMocks ShortUrlController shortUrlController;

    @Test
    void createShortUrl () throws NoSuchAlgorithmException {
        ShortUrlRequest shortUrlRequest = ShortUrlRequest.builder().url("www.google.com").build();
        ShortUrlResponse shortUrlResponse = ShortUrlResponse.builder().shortUrl("abc").build();

        when(shortUrlService.createShortUrl(shortUrlRequest)).thenReturn(shortUrlResponse);
        ResponseEntity<?> createResponse = shortUrlController.shortenUrl(shortUrlRequest);
        assertEquals(shortUrlResponse, ((ShortUrlResponse) createResponse.getBody()));
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
    }

    @Test
    void badRequestWhenShortUrlALreadyExists () throws NoSuchAlgorithmException {
        ShortUrlRequest shortUrlRequest = ShortUrlRequest.builder().url("www.google.com").customShortUrl("abc").build();
        ErrorResponse errorResponse = new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST), "Url Already exists");

        when(shortUrlService.createShortUrl(shortUrlRequest)).thenThrow(new ShortUrlExists("Url Already exists"));
        ResponseEntity<?> createResponse = shortUrlController.shortenUrl(shortUrlRequest);
        assertEquals(errorResponse, ((ErrorResponse) createResponse.getBody()));
        assertEquals(HttpStatus.BAD_REQUEST, createResponse.getStatusCode());
    }

    @Test
    void getShortUrl () {

        LongUrlResponse longUrlResponse = LongUrlResponse.builder().longUrl("www.google.com").build();

        when(shortUrlService.getLongUrl("abc")).thenReturn(longUrlResponse);
        ResponseEntity<?> createResponse = shortUrlController.getShortUrl("abc");
        assertEquals(longUrlResponse, ((LongUrlResponse) createResponse.getBody()));
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
    }
}
