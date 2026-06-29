package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.exceptions.BadRequest;
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
            throw new BadRequest("NOT_LOGIN");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            throw new BadRequest("USER_NOT_FOUND");
        }

        String secret = twoFactorService.generateSecret();

        user.setTwoFactorSecret(secret);
        user.setTwoFactorEnabled(false);

        userService.save(user);

        User updatedUser = userService.findByEmail(user.getEmail());
        session.setAttribute("user", updatedUser);
        session.setAttribute("authenticated", false);

        String qrUrl = twoFactorService.getQRBarcodeURL(
                updatedUser.getEmail(),
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
            throw new BadRequest("QR_GENERATION_FAILED");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifySetup(
            @RequestParam int code,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            throw new BadRequest("NOT_LOGIN");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            throw new BadRequest("USER_NOT_FOUND");
        }

        if (user.getTwoFactorSecret() == null || user.getTwoFactorSecret().isBlank()) {
            throw new BadRequest("2FA_NOT_SETUP");
        }
        System.out.println("OTP = " + code);

        System.out.println("SECRET = " + sessionUser.getTwoFactorSecret());

        System.out.println("TIME = " + java.time.LocalDateTime.now());
        boolean valid = twoFactorService.verifyCode(
                user.getTwoFactorSecret(),
                code
        );

        if (!valid) {
            throw new BadRequest("INVALID_OTP");
        }

        user.setTwoFactorEnabled(true);
        userService.save(user);

        User updatedUser = userService.findByEmail(user.getEmail());

        session.setAttribute("user", updatedUser);
        session.setAttribute("authenticated", true);

        userService.updateLastLogin(updatedUser);

        return ResponseEntity.ok("LOGIN_SUCCESS");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login2FA(
            @RequestParam int code,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            throw new BadRequest("NOT_LOGIN");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            throw new BadRequest("USER_NOT_FOUND");
        }

        if (user.getTwoFactorSecret() == null || user.getTwoFactorSecret().isBlank()) {
            throw new BadRequest("2FA_NOT_SETUP");
        }

        if (!Boolean.TRUE.equals(user.getTwoFactorEnabled())) {
            throw new BadRequest("2FA_NOT_ENABLED");
        }
        System.out.println("OTP = " + code);

        System.out.println("SECRET = " + sessionUser.getTwoFactorSecret());

        System.out.println("TIME = " + java.time.LocalDateTime.now());
        boolean valid = twoFactorService.verifyCode(
                user.getTwoFactorSecret(),
                code
        );

        if (!valid) {
            throw new BadRequest("INVALID_OTP");
        }

        session.setAttribute("authenticated", true);

        userService.updateLastLogin(user);

        User updatedUser = userService.findByEmail(user.getEmail());
        session.setAttribute("user", updatedUser);

        return ResponseEntity.ok("LOGIN_SUCCESS");
    }
}