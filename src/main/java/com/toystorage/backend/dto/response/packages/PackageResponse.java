package com.toystorage.backend.dto.response.packages;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PackageResponse {

    private Long id;
    private String packageCode;

    private Long transferId;
    private String transferCode;

    private Long warehouseId;
    private String warehouseName;

    private String status;

    private LocalDateTime packedAt;
    private LocalDateTime sealedAt;

    private String note;
}