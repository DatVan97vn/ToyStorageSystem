package com.toystorage.backend.services.auth;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TwoFactorService {

    private GoogleAuthenticator googleAuthenticator() {
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setWindowSize(3)
                .build();

        return new GoogleAuthenticator(config);
    }

    public String generateSecret() {
        GoogleAuthenticatorKey key = googleAuthenticator().createCredentials();
        return key.getKey();
    }

    public boolean verifyCode(String secret, int code) {
        if (secret == null || secret.isBlank()) {
            return false;
        }

        return googleAuthenticator().authorize(secret.trim(), code);
    }

    public String getQRBarcodeURL(String email, String secret) {
        String issuer = "ToyStorageSystem";

        String encodedIssuer = URLEncoder.encode(issuer, StandardCharsets.UTF_8);
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

        return "otpauth://totp/" + encodedIssuer + ":" + encodedEmail
                + "?secret=" + secret.trim()
                + "&issuer=" + encodedIssuer
                + "&algorithm=SHA1"
                + "&digits=6"
                + "&period=30";
    }
}