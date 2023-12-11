package com.example.demo.repository;

import com.example.demo.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    ShortUrl findByShortUrl(String shortUrl);
    ShortUrl findByFullUrl(String fullUrl);
}
