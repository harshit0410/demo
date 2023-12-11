package com.example.demo.service;

import com.example.demo.config.ShortUrlConfig;
import com.example.demo.dto.LongUrlResponse;
import com.example.demo.dto.ShortUrlRequest;
import com.example.demo.dto.ShortUrlResponse;
import com.example.demo.entity.ShortUrl;
import com.example.demo.exception.ShortUrlExists;
import com.example.demo.exception.ShortUrlNotFound;
import com.example.demo.repository.ShortUrlRepository;
import com.example.demo.util.ShortUrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlUtil shortUrlUtil;

    private String generateShortUrlKey (String longUrl) throws NoSuchAlgorithmException {
        String hash = shortUrlUtil.md5Hash(longUrl);
        while(shortUrlRepository.findByShortUrl(hash) != null) {
            longUrl = longUrl + shortUrlUtil.generateUniqueKey();
            hash = shortUrlUtil.md5Hash(longUrl);
        }

        return  hash;
    }
    public ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest) throws ShortUrlExists, NoSuchAlgorithmException {
        String longUrl = shortUrlRequest.getUrl();
        String newKey = "";

        if(shortUrlRequest.getCustomShortUrl() != null && !shortUrlRequest.getCustomShortUrl().equals("")) {
            if (shortUrlRepository.findByShortUrl(shortUrlRequest.getCustomShortUrl()) != null) {
                throw new ShortUrlExists("The given custom short url already exists");
            }
            newKey = shortUrlRequest.getCustomShortUrl();
        }
        else {
            newKey = generateShortUrlKey(shortUrlRequest.getUrl());
        }

        ShortUrl newEntity = ShortUrl.builder()
                .shortUrl(newKey).fullUrl(longUrl)
                .build();
        shortUrlRepository.save(newEntity);
        return ShortUrlResponse.builder().shortUrl(newKey).build();
    }

    public LongUrlResponse getLongUrl(String key) throws ShortUrlNotFound {
        ShortUrl entityInDb = shortUrlRepository.findByShortUrl(key);
        if(entityInDb == null) {
            throw new ShortUrlNotFound("The given short url does not exist - " + key);
        }
        return LongUrlResponse.builder().longUrl(entityInDb.getFullUrl()).build();
    }

}
