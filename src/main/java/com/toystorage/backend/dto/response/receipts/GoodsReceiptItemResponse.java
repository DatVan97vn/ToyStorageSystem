package com.toystorage.backend.dto.response.receipts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*
 * Response chi tiết item phiếu nhận hàng
 */
@Getter
@Setter
@Builder
public class GoodsReceiptItemResponse {

    private Long id;

    private Long goodsReceiptId;

    private Long productId;

    private String productName;

    private String productSku;

    private Integer putawayQuantity;

    private String productBarcode;

    private Integer expectedQuantity;

    private Integer receivedQuantity;

    private Long locationId;

    private String locationCode;
}