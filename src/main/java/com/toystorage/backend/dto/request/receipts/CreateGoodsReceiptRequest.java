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

    private Long supplierId;

    private Long warehouseId;

    private Long createdById;

    private Long businessStaffId;

    private String note;

    private List<CreateGoodsReceiptItemRequest> items;
}