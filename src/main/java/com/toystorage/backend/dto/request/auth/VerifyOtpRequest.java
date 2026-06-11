package com.toystorage.backend.dto.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/*
 * DTO verify OTP
 */

@Getter
@Setter

public class VerifyOtpRequest {

    @NotNull(message = "OTP code is required")

    private Integer code;
}