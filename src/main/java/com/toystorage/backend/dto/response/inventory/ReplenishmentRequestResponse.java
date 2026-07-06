package com.toystorage.backend.dto.response.inventory;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplenishmentRequestResponse {

    private Long id;

    private String requestCode;

    private Long requestWarehouseId;

    private String requestWarehouseName;

    private Long sourceWarehouseId;

    private String sourceWarehouseName;

    private Long productId;

    private String productName;

    private String productSku;

    private Integer currentQuantity;

    private Integer requestedQuantity;

    private Integer approvedQuantity;

    private Long requestedById;

    private String requestedByName;

    private Long approvedById;

    private String approvedByName;

    private Long stockTransferId;

    private String note;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;

    private LocalDateTime completedAt;
}