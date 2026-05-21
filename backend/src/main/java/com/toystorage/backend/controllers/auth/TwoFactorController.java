package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.services.auth.TwoFactorService;
import com.toystorage.backend.services.auth.UserService;
import com.toystorage.backend.utils.QRCodeUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/2fa")
@RequiredArgsConstructor
public class TwoFactorController {

    private final TwoFactorService twoFactorService;

    private final UserService userService;

    @PostMapping("/setup")
    public ResponseEntity<?> setup(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.badRequest().body("NOT_LOGIN");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("USER_NOT_FOUND");
        }

        String secret = twoFactorService.generateSecret();

        user.setTwoFactorSecret(secret);
        user.setTwoFactorEnabled(false);

        userService.save(user);

        String qrUrl = twoFactorService.getQRBarcodeURL(
                user.getEmail(),
                secret
        );

        try {
            String qrImage = QRCodeUtil.generateQRImage(qrUrl);

            return ResponseEntity.ok(
                    Map.of(
                            "url", qrUrl,
                            "qr", qrImage
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("QR_GENERATION_FAILED");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifySetup(
            @RequestParam int code,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.badRequest().body("NOT_LOGIN");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("USER_NOT_FOUND");
        }

        boolean valid = twoFactorService.verifyCode(
                user.getTwoFactorSecret(),
                code
        );

        if (!valid) {
            return ResponseEntity.badRequest().body("INVALID_OTP");
        }

        user.setTwoFactorEnabled(true);

        userService.save(user);

        session.setAttribute("user", user);

        return ResponseEntity.ok("2FA_ENABLED");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login2FA(
            @RequestParam int code,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.badRequest().body("NOT_LOGIN");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("USER_NOT_FOUND");
        }

        boolean valid = twoFactorService.verifyCode(
                user.getTwoFactorSecret(),
                code
        );

        if (!valid) {
            return ResponseEntity.badRequest().body("INVALID_OTP");
        }

        session.setAttribute("authenticated", true);

        userService.updateLastLogin(user);

        return ResponseEntity.ok("LOGIN_SUCCESS");
    }
}