package com.toystorage.backend.dto.request.packages;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO tạo kiện hàng
 */

@Getter
@Setter

public class CreatePackageRequest {

    /*
     * Phiếu điều kho
     */
    private Long transferId;

    /*
     * Ghi chú
     */
    private String note;
}