package com.toystorage.backend.dto.response.manifests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManifestTransferResponse {

    private Long id;

    private Long manifestId;

    private Long transferId;

    private String transferCode;

    private String status;

    private Long fromWarehouseId;

    private String fromWarehouseName;

    private Long toWarehouseId;

    private String toWarehouseName;
}