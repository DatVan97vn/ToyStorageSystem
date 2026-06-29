package com.toystorage.backend.services.auth;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TwoFactorService {

    public String generateSecret() {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();

        return key.getKey();
    }

    public boolean verifyCode(String secret, int code) {
        if (secret == null || secret.isBlank()) {
            return false;
        }

        System.out.println("SECRET VERIFY = " + secret);
        System.out.println("CODE VERIFY = " + code);
        System.out.println("SERVER TIME = " + java.time.LocalDateTime.now());

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

        boolean result = googleAuthenticator.authorize(secret, code);

        System.out.println("OTP VALID = " + result);

        return result;
    }

    public String getQRBarcodeURL(String email, String secret) {
        String issuer = "ToyStorageSystem";

        String encodedIssuer = URLEncoder.encode(issuer, StandardCharsets.UTF_8);
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

        return "otpauth://totp/" + encodedIssuer + ":" + encodedEmail
                + "?secret=" + secret
                + "&issuer=" + encodedIssuer;
    }
}