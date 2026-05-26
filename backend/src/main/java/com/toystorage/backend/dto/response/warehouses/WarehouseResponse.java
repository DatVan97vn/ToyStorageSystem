package com.toystorage.backend.dto.response.warehouses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WarehouseResponse {

    private Long id;

    private String name;

    private String code;

    private String type;

    private String phone;

    private String address;

    private Long managerId;

    private String managerName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}