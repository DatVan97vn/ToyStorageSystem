package com.example.project4.controller;

import com.example.project4.entity.User;
import com.example.project4.service.TwoFactorService;
import com.example.project4.service.UserService;
import com.example.project4.util.QRCodeUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

@RequestMapping("/2fa")
public class TwoFactorController {

    @Autowired
    private TwoFactorService twoFactorService;

    @Autowired
    private UserService userService;

    @GetMapping("/setup")
    public Map<String, String> setup(@RequestParam String username) {

        User user = userService.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String secret = twoFactorService.generateSecret();
        user.setSecret(secret);
        userService.save(user);

        String qrUrl = twoFactorService.getQRBarcodeURL(username, secret);

        try {
            String qrImage = QRCodeUtil.generateQRImage(qrUrl);

            return Map.of(
                    "url", qrUrl,
                    "qr", qrImage
            );

        } catch (Exception e) {
            throw new RuntimeException("QR generation failed", e);
        }
    }


    @PostMapping("/verify")
    public String verify(HttpSession session,
                         @RequestParam int code) {

        User user = (User) session.getAttribute("user");

        if (user == null) return "NOT LOGIN";

        boolean ok = twoFactorService.verifyCode(user.getSecret(), code);

        if (ok) {
            user.setUsing2FA(true);
            userService.save(user);
            return "2FA ENABLED";
        }

        return "INVALID OTP";
    }

    // login OTP
    @PostMapping("/login")
    public String login2FA(HttpSession session,
                           @RequestParam int code) {

        User user = (User) session.getAttribute("user");

        if (user == null) return "NOT LOGIN";

        boolean ok = twoFactorService.verifyCode(user.getSecret(), code);

        return ok ? "LOGIN SUCCESS" : "INVALID OTP";
    }
}