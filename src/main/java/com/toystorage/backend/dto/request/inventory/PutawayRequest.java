package com.toystorage.backend.dto.request.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutawayRequest {

    private Long goodsReceiptItemId;

    private Long locationId;

    private Integer quantity;

    private Long putawayById;
}