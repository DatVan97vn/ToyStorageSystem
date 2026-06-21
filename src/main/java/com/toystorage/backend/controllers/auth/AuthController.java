package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.dto.request.auth.*;
import com.toystorage.backend.enums.users.UserStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.auth.AuthMapper;
import com.toystorage.backend.mapper.auth.UserMapper;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.services.auth.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userService.findByEmail(request.getEmail()) != null) {
            throw new BadRequest("EMAIL_ALREADY_EXISTS");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BadRequest("PASSWORD_TOO_SHORT");
        }

        User user = authMapper.toUser(
                request,
                userService.encodePassword(request.getPassword())
        );

        userService.save(user);

        return ResponseEntity.ok("REGISTER_SUCCESS");
    }

    @PostMapping("/register-staff")
    public ResponseEntity<?> registerStaff(@RequestBody RegisterStaffRequest request) {
        if (userService.findByEmail(request.getEmail()) != null) {
            throw new BadRequest("EMAIL_ALREADY_EXISTS");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BadRequest("PASSWORD_TOO_SHORT");
        }

        User staff = authMapper.toStaff(
                request,
                userService.encodePassword(request.getPassword())
        );

        User savedStaff = userService.saveStaffWithRole(staff, request.getRole());

        return ResponseEntity.ok(userMapper.toResponse(savedStaff));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpSession session
    ) {
        User user = userService.findByEmail(request.getEmail());

        if (user == null) {
            throw new BadRequest("USER_NOT_FOUND");
        }

        if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new BadRequest("INVALID_PASSWORD");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BadRequest("ACCOUNT_NOT_ACTIVE");
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
            @RequestBody ChangePasswordRequest request,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            throw new BadRequest("NOT_LOGIN");
        }

        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            throw new BadRequest("PASSWORD_TOO_SHORT");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            throw new BadRequest("USER_NOT_FOUND");
        }

        userService.changePassword(user, request.getNewPassword());
        user.setFirstLogin(false);

        session.setAttribute("user", user);

        return ResponseEntity.ok("PASSWORD_CHANGED");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");

        if (user == null) {
            throw new BadRequest("NOT_LOGIN");
        }

        if (authenticated == null || !authenticated) {
            throw new BadRequest("NOT_AUTHENTICATED");
        }

        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("LOGOUT_SUCCESS");
    }
}