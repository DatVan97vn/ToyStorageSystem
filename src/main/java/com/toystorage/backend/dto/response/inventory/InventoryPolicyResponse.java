package com.toystorage.backend.dto.response.inventory;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryPolicyResponse {

    private Long id;

    private Long warehouseId;

    private String warehouseName;

    private Long productId;

    private String productName;

    private String productSku;

    private Integer minimumQuantity;

    private Integer maximumQuantity;

    private Integer reorderQuantity;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}