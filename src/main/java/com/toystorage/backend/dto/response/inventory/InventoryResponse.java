package com.toystorage.backend.dto.response.inventory;

import lombok.Data;

@Data
public class InventoryResponse {

    private Long id;

    private Long productId;
    private String productName;

    private Long warehouseId;
    private String warehouseName;

    private Integer quantity;
}