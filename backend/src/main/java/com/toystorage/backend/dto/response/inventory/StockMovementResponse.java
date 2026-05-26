package com.toystorage.backend.dto.response.inventory;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/*
 * Response lịch sử biến động kho
 */

@Getter
@Setter
@Builder

public class StockMovementResponse {

    private Long id;

    private Long warehouseId;

    private String warehouseName;

    private Long productId;

    private String productName;

    private String movementType;

    private Integer quantity;

    private String referenceType;

    private Long referenceId;

    private LocalDateTime createdAt;
}