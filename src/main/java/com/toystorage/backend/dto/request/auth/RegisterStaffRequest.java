package com.toystorage.backend.dto.request.auth;

import lombok.Data;

@Data
public class RegisterStaffRequest {
    private String name;
    private String email;
    private String password;
    private String role;
}