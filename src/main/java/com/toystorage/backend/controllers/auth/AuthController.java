package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.enums.users.UserStatus;
import com.toystorage.backend.exceptions.BadRequest;
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

    // ================= REGISTER (PUBLIC) =================
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String Name,
            @RequestParam String email,
            @RequestParam String password
    ) {
        if (userService.findByEmail(email) != null) {
            throw new BadRequest("EMAIL_ALREADY_EXISTS");
        }

        if (password == null || password.length() < 6) {
            throw new BadRequest("PASSWORD_TOO_SHORT");
        }

        User user = new User();
        user.setName(Name);
        user.setEmail(email.trim());
        user.setPassword(userService.encodePassword(password));
        user.setStatus(UserStatus.ACTIVE);
        user.setFirstLogin(false);
        user.setTwoFactorEnabled(false);

        userService.save(user);

        return ResponseEntity.ok("REGISTER_SUCCESS");
    }

    // ================= REGISTER STAFF =================
    @PostMapping("/register-staff")
    public ResponseEntity<?> registerStaff(
            @RequestParam String Name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role
    ) {
        if (userService.findByEmail(email) != null) {
            throw new BadRequest("EMAIL_ALREADY_EXISTS");
        }

        if (password == null || password.length() < 6) {
            throw new BadRequest("PASSWORD_TOO_SHORT");
        }

        User staff = new User();
        staff.setName(Name);
        staff.setEmail(email.trim());
        staff.setPassword(userService.encodePassword(password));
        staff.setStatus(UserStatus.ACTIVE);
        staff.setFirstLogin(true);
        staff.setTwoFactorEnabled(false);

        User savedStaff = userService.saveStaffWithRole(staff, role);

        return ResponseEntity.ok(
                userMapper.toResponse(savedStaff)
        );
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new BadRequest("USER_NOT_FOUND");
        }

        if (!userService.checkPassword(password, user.getPassword())) {
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

    // ================= CHANGE PASSWORD =================
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam String newPassword,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            throw new BadRequest("NOT_LOGIN");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new BadRequest("PASSWORD_TOO_SHORT");
        }

        User user = userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            throw new BadRequest("USER_NOT_FOUND");
        }

        userService.changePassword(user, newPassword);
        user.setFirstLogin(false);

        session.setAttribute("user", user);

        return ResponseEntity.ok("PASSWORD_CHANGED");
    }

    // ================= CURRENT USER =================
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

        return ResponseEntity.ok(
                userMapper.toResponse(user)
        );
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();

        return ResponseEntity.ok("LOGOUT_SUCCESS");
    }
}