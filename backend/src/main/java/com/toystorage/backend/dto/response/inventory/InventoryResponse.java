package com.toystorage.backend.dto.response.inventory;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*
 * Response tồn kho
 */

@Getter
@Setter
@Builder

public class InventoryResponse {

    private Long warehouseId;

    private String warehouseName;

    private Long productId;

    private String productName;

    private Integer quantity;
}