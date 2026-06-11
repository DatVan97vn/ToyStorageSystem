package com.toystorage.backend.dto.request.receipts;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO cập nhật số lượng thực nhận
 *
 * Dùng cho API:
 * PUT /api/goods-receipts/items/{itemId}/receive
 */
@Getter
@Setter
public class ReceiveGoodsReceiptItemRequest {

    /*
     * Số lượng thực nhận
     */
    private Integer receivedQuantity;
}