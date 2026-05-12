package com.example.project4.controller;

import com.example.project4.entity.User;
import com.example.project4.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
    origins = "http://localhost:5173",
    allowCredentials = "true"
)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        User user = userService.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            return "LOGIN FAILED";
        }

        session.setAttribute("user", user);
        session.setAttribute("authenticated", false);

        // bắt đổi password lần đầu
        if (user.isFirstLogin()) {
            return "REQUIRE_CHANGE_PASSWORD";
        }

        // chưa setup 2FA
        if (user.getSecret() == null || user.getSecret().isEmpty()) {
            return "REQUIRE_2FA_SETUP";
        }

        // đã setup 2FA
        return "REQUIRE_OTP";
    }
    @PostMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "LOGOUT SUCCESS";
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,
                                 HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) return "NOT LOGIN";

        User user = userService.findByUsername(sessionUser.getUsername());

        if (user == null) return "USER NOT FOUND";

        user.setPassword(newPassword);
        user.setFirstLogin(false);

        userService.save(user);

        session.setAttribute("user", user);

        return "PASSWORD CHANGED";
    }
    @GetMapping("/me")
    public String me(HttpSession session) {

        User user = (User) session.getAttribute("user");
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");

        if (user == null) {
            return "NOT_LOGIN";
        }

        if (authenticated == null || !authenticated) {
            return "NOT_AUTHENTICATED";
        }

        return "LOGIN";
    }
}