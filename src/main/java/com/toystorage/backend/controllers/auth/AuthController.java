package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.dto.request.auth.*;
import com.toystorage.backend.enums.users.UserStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.auth.AuthMapper;
import com.toystorage.backend.mapper.auth.UserMapper;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.services.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
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

        // Bắt buộc staff/admin lần đầu đăng nhập phải đổi mật khẩu
        staff.setFirstLogin(true);

        // Đảm bảo tài khoản active
        staff.setStatus(UserStatus.ACTIVE);

        // Chưa xác thực 2FA
        staff.setTwoFactorEnabled(false);

        User savedStaff = userService.saveStaffWithRole(staff, request.getRole());

        return ResponseEntity.ok(userMapper.toResponse(savedStaff));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest httpRequest
    ) {
        User user = userService.findByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new BadRequest("USER_NOT_FOUND");
        }

        if (!userService.checkPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequest("INVALID_PASSWORD");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BadRequest("ACCOUNT_NOT_ACTIVE");
        }

        // Xóa session cũ nếu có
        HttpSession oldSession = httpRequest.getSession(false);

        if (oldSession != null) {
            oldSession.invalidate();
        }

        // Tạo session mới cho lần login mới
        HttpSession session = httpRequest.getSession(true);

        User freshUser = userService.findByEmail(loginRequest.getEmail());

        session.setAttribute("user", freshUser);

        // Quan trọng: mỗi lần login đều chưa xác thực OTP
        session.setAttribute("authenticated", false);

        // Lần đầu đăng nhập: bắt đổi mật khẩu
        if (Boolean.TRUE.equals(freshUser.getFirstLogin())) {
            return ResponseEntity.ok("REQUIRE_CHANGE_PASSWORD");
        }

        // Chưa setup 2FA: bắt setup 2FA
        if (
                freshUser.getTwoFactorSecret() == null
                        || freshUser.getTwoFactorSecret().isBlank()
                        || !Boolean.TRUE.equals(freshUser.getTwoFactorEnabled())
        ) {
            return ResponseEntity.ok("REQUIRE_2FA_SETUP");
        }

        // Đã setup 2FA: mỗi lần đăng nhập đều bắt nhập OTP
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

        if (userService.checkPassword(request.getNewPassword(), user.getPassword())) {
            throw new BadRequest("NEW_PASSWORD_MUST_BE_DIFFERENT");
        }

        userService.changePassword(user, request.getNewPassword());

        User updatedUser = userService.findByEmail(user.getEmail());
        session.setAttribute("user", updatedUser);
        session.setAttribute("authenticated", false);

        if (
                updatedUser.getTwoFactorSecret() == null
                        || updatedUser.getTwoFactorSecret().isBlank()
                        || !Boolean.TRUE.equals(updatedUser.getTwoFactorEnabled())
        ) {
            return ResponseEntity.ok("REQUIRE_2FA_SETUP");
        }

        return ResponseEntity.ok("REQUIRE_OTP");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {

        User user = (User) session.getAttribute("user");
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");

        if (user == null) {
            throw new BadRequest("NOT_LOGIN");
        }

        if (!Boolean.TRUE.equals(authenticated)) {
            throw new BadRequest("NOT_AUTHENTICATED");
        }

        User updatedUser = userService.findByEmail(user.getEmail());

        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok("LOGOUT_SUCCESS");
    }
}