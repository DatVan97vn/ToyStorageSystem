package com.toystorage.backend.dto.request.transfers;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO sản phẩm trong phiếu điều kho
 */

@Getter
@Setter

public class CreateTransferItemRequest {

    /*
     * Sản phẩm
     */
    private Long productId;

    /*
     * Số lượng
     */
    private Integer quantity;
}