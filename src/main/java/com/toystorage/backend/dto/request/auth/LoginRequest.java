package com.toystorage.backend.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/*
 * DTO login
 */

@Getter
@Setter

public class LoginRequest {

    /*
     * Email đăng nhập
     */
    @Email(message = "Invalid email")

    @NotBlank(message = "Email is required")

    private String email;

    /*
     * Password
     */
    @NotBlank(message = "Password is required")

    private String password;
}