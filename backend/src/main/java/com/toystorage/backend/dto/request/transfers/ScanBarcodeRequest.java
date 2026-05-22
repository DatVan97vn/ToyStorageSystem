package com.toystorage.backend.dto.request.transfers;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO scan barcode
 */

@Getter
@Setter

public class ScanBarcodeRequest {

    /*
     * Mã barcode
     */
    private String barcode;

    /*
     * Số lượng scan
     */
    private Integer quantity;

    /*
     * Phiếu điều kho
     */
    private Long transferId;
}