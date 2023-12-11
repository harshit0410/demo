package com.example.demo.util;

import com.example.demo.config.ShortUrlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Component
public class ShortUrlUtil {
    private final ShortUrlConfig config;

    @Autowired
    public ShortUrlUtil (ShortUrlConfig config) {
        this.config = config;
    }

    public String generateUniqueKey() {
        int keyLength = config.getShortUrlLength();
        String allowedCharacters = config.getAllowedCharacters();

        StringBuilder keyBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < keyLength; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            keyBuilder.append(allowedCharacters.charAt(randomIndex));
        }

        return keyBuilder.toString();
    }

    public String md5Hash (String key) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes());
        byte[] digest = md.digest();
        String myHash = String.format("%032x", new BigInteger(1, digest));
        return  myHash.substring(0, 7);
    }
}
