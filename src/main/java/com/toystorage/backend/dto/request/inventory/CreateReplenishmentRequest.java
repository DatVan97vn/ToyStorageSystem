package com.toystorage.backend.dto.request.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReplenishmentRequest {

    private Long requestWarehouseId;

    private Long productId;

    private Integer currentQuantity;

    private Integer requestedQuantity;

    private Long requestedById;

    private String note;
}