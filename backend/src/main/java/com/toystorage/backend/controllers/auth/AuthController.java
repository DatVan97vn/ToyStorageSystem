package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.enums.users.UserStatus;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.services.auth.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("USER_NOT_FOUND");
        }

        if (!userService.checkPassword(password, user.getPassword())) {
            return ResponseEntity.badRequest().body("INVALID_PASSWORD");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            return ResponseEntity.badRequest().body("ACCOUNT_NOT_ACTIVE");
        }

        session.setAttribute("user", user);
        session.setAttribute("authenticated", false);

        if (Boolean.TRUE.equals(user.getFirstLogin())) {
            return ResponseEntity.ok("REQUIRE_CHANGE_PASSWORD");
        }

        if (
                user.getTwoFactorSecret() == null
                        || user.getTwoFactorSecret().isBlank()
                        || !Boolean.TRUE.equals(user.getTwoFactorEnabled())
        ) {
            return ResponseEntity.ok("REQUIRE_2FA_SETUP");
        }

        return ResponseEntity.ok("REQUIRE_OTP");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam String newPassword,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.badRequest().body("NOT_LOGIN");
        }

        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body("PASSWORD_TOO_SHORT");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("USER_NOT_FOUND");
        }

        userService.changePassword(user, newPassword);

        session.setAttribute("user", user);

        return ResponseEntity.ok("PASSWORD_CHANGED");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");

        if (user == null) {
            return ResponseEntity.badRequest().body("NOT_LOGIN");
        }

        if (authenticated == null || !authenticated) {
            return ResponseEntity.badRequest().body("NOT_AUTHENTICATED");
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();

        return ResponseEntity.ok("LOGOUT_SUCCESS");
    }
}