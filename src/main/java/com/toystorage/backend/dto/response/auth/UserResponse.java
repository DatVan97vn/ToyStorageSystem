package com.toystorage.backend.dto.response.auth;

import com.toystorage.backend.enums.users.UserStatus;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private UserStatus status;
    private Boolean firstLogin;
    private Boolean twoFactorEnabled;
}