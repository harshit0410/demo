package com.example.demo.service;

import com.example.demo.dto.LongUrlResponse;
import com.example.demo.dto.ShortUrlRequest;
import com.example.demo.dto.ShortUrlResponse;
import com.example.demo.entity.ShortUrl;
import com.example.demo.exception.ShortUrlExists;
import com.example.demo.exception.ShortUrlNotFound;
import com.example.demo.repository.ShortUrlRepository;
import com.example.demo.util.ShortUrlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShortUrlServiceTest {
    @Mock
    ShortUrlRepository shortUrlRepository;
    @Mock
    ShortUrlUtil shortUrlUtil;

    @InjectMocks
    ShortUrlService shortUrlService;

    @Test
    void createShortUrl () throws NoSuchAlgorithmException {
        ShortUrlRequest shortUrlRequest = ShortUrlRequest.builder().url("www.google.com").build();
        String hash = "abc";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortUrl(hash);
        shortUrl.setFullUrl(shortUrlRequest.getUrl());
        shortUrl.setId(1L);
        when(shortUrlUtil.md5Hash(shortUrlRequest.getUrl())).thenReturn(hash);
        when(shortUrlRepository.findByShortUrl(hash)).thenReturn(null);
        when(shortUrlRepository.save(any())).thenReturn(shortUrl);
        ShortUrlResponse response = shortUrlService.createShortUrl(shortUrlRequest);
        assertEquals(hash, response.getShortUrl());
    }

    @Test
    void createShortUrlWithCustomUrl () throws NoSuchAlgorithmException {
        ShortUrlRequest shortUrlRequest = ShortUrlRequest.builder().url("www.google.com").customShortUrl("abc").build();
        String hash = "abc";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortUrl(hash);
        shortUrl.setFullUrl(shortUrlRequest.getUrl());
        shortUrl.setId(1L);

        when(shortUrlRepository.findByShortUrl(hash)).thenReturn(null);
        when(shortUrlRepository.save(any())).thenReturn(shortUrl);
        ShortUrlResponse response = shortUrlService.createShortUrl(shortUrlRequest);
        assertEquals(hash, response.getShortUrl());
    }

    @Test
    void customUrlAlreadyExists () {
        ShortUrlRequest shortUrlRequest = ShortUrlRequest.builder().url("www.google.com").customShortUrl("abc").build();
        String hash = "abc";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortUrl(hash);
        shortUrl.setFullUrl(shortUrlRequest.getUrl());
        shortUrl.setId(1L);

        when(shortUrlRepository.findByShortUrl(hash)).thenReturn(shortUrl);
        assertThrows(ShortUrlExists.class, () -> shortUrlService.createShortUrl(shortUrlRequest));
    }

    @Test
    void getLongUrl () {
        String hash = "abc";
        String longUrl = "www.google.com";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortUrl(hash);
        shortUrl.setFullUrl(longUrl);
        shortUrl.setId(1L);

        when(shortUrlRepository.findByShortUrl(hash)).thenReturn(shortUrl);
        LongUrlResponse response = shortUrlService.getLongUrl(hash);

        assertEquals(longUrl, response.getLongUrl());
    }

    @Test
    void shortUrlNotFound () {
        String hash = "abc";
        when(shortUrlRepository.findByShortUrl(hash)).thenReturn(null);

        assertThrows(ShortUrlNotFound.class, () ->  shortUrlService.getLongUrl(hash));
    }
}
