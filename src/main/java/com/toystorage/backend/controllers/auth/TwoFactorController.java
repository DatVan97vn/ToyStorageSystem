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

    // ================= SETUP 2FA =================
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

        session.setAttribute("user", user);

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
            throw new BadRequest("QR_GENERATION_FAILED");
        }
    }

    // ================= VERIFY 2FA SETUP =================
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

        boolean valid = twoFactorService.verifyCode(
                user.getTwoFactorSecret(),
                code
        );

        if (!valid) {
            throw new BadRequest("INVALID_OTP");
        }

        user.setTwoFactorEnabled(true);

        userService.save(user);

        session.setAttribute("user", user);

        return ResponseEntity.ok("2FA_ENABLED");
    }

    // ================= LOGIN 2FA =================
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