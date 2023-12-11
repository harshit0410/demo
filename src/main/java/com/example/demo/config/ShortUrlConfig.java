package com.example.demo.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "short-url")
@Getter
@Setter
public class ShortUrlConfig {
    private int shortUrlLength;
    private String allowedCharacters;
}
