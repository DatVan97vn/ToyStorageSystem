package com.toystorage.backend.dto.request.receipts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGoodsReceiptItemRequest {

    private Long productId;

    private Integer expectedQuantity;

    private Integer receivedQuantity;

    private Long locationId;
}