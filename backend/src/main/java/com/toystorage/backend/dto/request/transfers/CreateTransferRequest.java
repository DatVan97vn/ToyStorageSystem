package com.toystorage.backend.dto.request.transfers;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 * DTO tạo phiếu điều kho
 */

@Getter
@Setter

public class CreateTransferRequest {

    /*
     * Kho xuất
     */
    private Long fromWarehouseId;

    /*
     * Kho nhập
     */
    private Long toWarehouseId;

    /*
     * Danh sách sản phẩm
     */
    private List<CreateTransferItemRequest> items;
}