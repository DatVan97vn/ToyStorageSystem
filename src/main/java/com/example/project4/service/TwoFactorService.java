package com.example.project4.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorService {
//Tạo secret key để liên kết giữa server và Google Authenticator
    public String generateSecret() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }
//Kiểm tra mã OTP user nhập có đúng hay không
    public boolean verifyCode(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, code);
    }
//Tạo link QR để Google Authenticator hiểu và lưu tài khoản.

    public String getQRBarcodeURL(String username, String secret) {
        String issuer = "Project4";
        return "otpauth://totp/" + issuer + ":" + username +
                "?secret=" + secret +
                "&issuer=" + issuer;
    }
}