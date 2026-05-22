package com.toystorage.backend.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*
 * Response login
 */

@Getter
@Setter
@Builder

public class LoginResponse {

    private String message;

    private Boolean authenticated;

    private String role;

    private String name;
}