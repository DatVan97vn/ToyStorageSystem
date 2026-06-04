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
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    // ================= REGISTER (PUBLIC) =================
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String Name,
            @RequestParam String email,
            @RequestParam String password
    ) {

        // check email
        if (userService.findByEmail(email) != null) {
            return ResponseEntity.badRequest()
                    .body("EMAIL_ALREADY_EXISTS");
        }

        // validate password
        if (password == null || password.length() < 6) {
            return ResponseEntity.badRequest()
                    .body("PASSWORD_TOO_SHORT");
        }

        // create user
        User user = new User();
        user.setName(Name);
        user.setEmail(email);

        // encode password
        user.setPassword(
                userService.encodePassword(password)
        );

        user.setStatus(UserStatus.ACTIVE);

        // default values
        user.setFirstLogin(false);
        user.setTwoFactorEnabled(false);

        // Lưu xuống DB (Hàm save này xử lý gán role mặc định an toàn bên Service nếu cần)
        userService.save(user);

        return ResponseEntity.ok("REGISTER_SUCCESS");
    }

    // ================= REGISTER STAFF (FOR IT MANAGER) =================
    /*
     * API chuyên dụng để Admin/IT Manager khởi tạo tài khoản nhân sự
     * Gửi dữ liệu qua Postman dạng Body -> x-www-form-urlencoded
     */
    @PostMapping("/register-staff")
    public ResponseEntity<?> registerStaff(
            @RequestParam String Name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role
    ) {
        // 1. Kiểm tra trùng lặp tài khoản
        if (userService.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body("EMAIL_ALREADY_EXISTS");
        }

        // 2. Kiểm tra độ dài mật khẩu bảo mật
        if (password == null || password.length() < 6) {
            return ResponseEntity.badRequest().body("PASSWORD_TOO_SHORT");
        }

        // 3. Khởi tạo thực thể nhân viên
        User staff = new User();
        staff.setName(Name);
        staff.setEmail(email.trim());
        staff.setPassword(userService.encodePassword(password));
        staff.setStatus(UserStatus.ACTIVE);

        // 4. Bắt buộc đổi mật khẩu trong lần đầu đăng nhập hệ thống quản trị kho
        staff.setFirstLogin(true); 
        staff.setTwoFactorEnabled(false);

        // 5. Lưu xuống Database thông qua hàm xử lý chuyên biệt ép kiểu dữ liệu sang cấu trúc Role
        try {
            User savedStaff = userService.saveStaffWithRole(staff, role);
            return ResponseEntity.ok(savedStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
            return ResponseEntity.badRequest()
                    .body("USER_NOT_FOUND");
        }

        // check password
        if (!userService.checkPassword(
                password,
                user.getPassword()
        )) {

            return ResponseEntity.badRequest()
                    .body("INVALID_PASSWORD");
        }

        // check status
        if (user.getStatus() != UserStatus.ACTIVE) {
            return ResponseEntity.badRequest()
                    .body("ACCOUNT_NOT_ACTIVE");
        }

        // save session
        session.setAttribute("user", user);
        session.setAttribute("authenticated", true);

        return ResponseEntity.ok(user);
    }

    // ================= CHANGE PASSWORD =================
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam String newPassword,
            HttpSession session
    ) {

        User sessionUser =
                (User) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.badRequest()
                    .body("NOT_LOGIN");
        }

        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest()
                    .body("PASSWORD_TOO_SHORT");
        }

        User user =
                userService.findByEmail(sessionUser.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("USER_NOT_FOUND");
        }

        // update password
        userService.changePassword(user, newPassword);

        return ResponseEntity.ok("PASSWORD_CHANGED");
    }

    // ================= CURRENT USER =================
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {

        User user =
                (User) session.getAttribute("user");

        Boolean authenticated =
                (Boolean) session.getAttribute("authenticated");

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("NOT_LOGIN");
        }

        if (authenticated == null || !authenticated) {
            return ResponseEntity.badRequest()
                    .body("NOT_AUTHENTICATED");
        }

        return ResponseEntity.ok(user);
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok("LOGOUT_SUCCESS");
    }
}