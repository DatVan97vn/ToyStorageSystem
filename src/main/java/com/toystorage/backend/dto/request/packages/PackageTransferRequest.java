package com.toystorage.backend.dto.request.packages;

import lombok.Data;

@Data
public class PackageTransferRequest {

    private Long packageId;

    private Long transferItemId;

    private Integer quantity;
}