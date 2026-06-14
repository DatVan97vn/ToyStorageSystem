package com.toystorage.backend.dto.response.warehouses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponse {

    private Long id;

    private String code;

    private String name;

    private String address;

    private String phone;

    private String status;

    private Long totalLocations;

    private Long emptyLocations;

    private Long usedLocations;

    private String type;

    private Long managerId;
    private String managerName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}