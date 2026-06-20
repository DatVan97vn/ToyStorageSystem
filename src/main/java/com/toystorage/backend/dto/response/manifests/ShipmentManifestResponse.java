package com.toystorage.backend.dto.response.manifests;

import com.toystorage.backend.enums.manifests.ManifestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentManifestResponse {

    private Long id;

    private String manifestCode;

    private Long fromWarehouseId;
    private String fromWarehouseName;

    private Long toWarehouseId;
    private String toWarehouseName;

    private ManifestStatus status;

    private Long createdById;
    private String createdByUsername;

    private LocalDateTime createdAt;
}