package com.toystorage.backend.dto.response.suppliers;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SupplierResponse {

    private Long id;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String taxCode;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}