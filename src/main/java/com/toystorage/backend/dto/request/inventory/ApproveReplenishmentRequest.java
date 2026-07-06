package com.toystorage.backend.dto.request.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveReplenishmentRequest {

    private Long sourceWarehouseId;

    private Integer approvedQuantity;

    private Long approvedById;

    private String note;
}