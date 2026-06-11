package com.toystorage.backend.dto.request.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role; // "ADMIN", "WAREHOUSE_MANAGER", "STAFF"
}