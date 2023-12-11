package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exception.ShortUrlExists;
import com.example.demo.exception.ShortUrlNotFound;
import com.example.demo.service.ShortUrlService;
import com.example.demo.util.BuildError;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;


@RestController
public class ShortUrlController {
    @Autowired
    ShortUrlService shortUrlService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortUrlController.class);

    @RequestMapping(value = "/shortUrl", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity <?> shortenUrl(
            @Valid @RequestBody final ShortUrlRequest request) {
        try {
            ShortUrlResponse response = shortUrlService.createShortUrl(request);
            return ResponseEntity.ok(response);
        }
        catch(ShortUrlExists err) {
            LOGGER.error(err.getMessage());
            return new ResponseEntity <> (BuildError.buildError(err.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
        } catch (NoSuchAlgorithmException err) {
            LOGGER.error(err.getMessage());
            return new ResponseEntity <> (BuildError.buildError("Something went wrong", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value="/shortUrl/{url}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getShortUrl (@PathVariable String url) {
        try {
            LongUrlResponse response =  shortUrlService.getLongUrl(url);
            return new ResponseEntity <> (response, HttpStatus.OK);
//            return ResponseEntity.status(HttpStatus.MOVED_TEMPORARILY).header("connection", "close").header(HttpHeaders.LOCATION, response.getLongUrl()).build();
        }
        catch (ShortUrlNotFound err) {
            LOGGER.error(err.getMessage());
            return new ResponseEntity <> (BuildError.buildError(err.getMessage(), String.valueOf(HttpStatus.NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }
}
