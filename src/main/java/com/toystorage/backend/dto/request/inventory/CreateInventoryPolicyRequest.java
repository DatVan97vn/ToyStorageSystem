package com.toystorage.backend.dto.request.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInventoryPolicyRequest {

    private Long warehouseId;

    private Long productId;

    private Integer minimumQuantity;

    private Integer maximumQuantity;

    private Integer reorderQuantity;

    private Boolean active;
}