package com.plantrip.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {
    private final String secret;

    public JwtConfig(@Value("${jwt.secret}")String secret) {
        this.secret = secret;
    }

    private SecretKey secretKey() {
        return new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }
}
