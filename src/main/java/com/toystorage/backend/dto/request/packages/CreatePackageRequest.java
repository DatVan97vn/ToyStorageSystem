package com.toystorage.backend.dto.request.packages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePackageRequest {

    private Long transferId;

    private Long warehouseId;

    private Integer quantity;

    private String note;
}