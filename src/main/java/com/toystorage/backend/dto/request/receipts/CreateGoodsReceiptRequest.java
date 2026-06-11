package com.toystorage.backend.dto.request.receipts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 * DTO tạo phiếu nhận hàng
 */
@Getter
@Setter
public class CreateGoodsReceiptRequest {

    private Long manifestId;

    private Long transferId;

    private Long warehouseId;

    private Long receivedById;

    private Long checkedById;

    private String note;

    private List<CreateGoodsReceiptItemRequest> items;
}