package com.toystorage.backend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/*
 * DTO đổi mật khẩu
 */

@Getter
@Setter

public class ChangePasswordRequest {

    @NotBlank(message = "Password is required")

    @Size(min = 6, message = "Password minimum 6 characters")

    private String newPassword;
}