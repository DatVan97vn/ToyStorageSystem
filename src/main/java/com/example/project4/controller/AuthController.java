package com.example.project4.controller;

import com.example.project4.entity.User;
import com.example.project4.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

        if (user.isUsing2FA()) {
            return "REQUIRE_OTP";
        }

        return "LOGIN SUCCESS";
    }
}