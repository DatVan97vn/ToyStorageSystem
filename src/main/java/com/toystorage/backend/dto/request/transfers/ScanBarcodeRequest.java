package com.toystorage.backend.dto.request.transfers;

import lombok.Data;

@Data
public class ScanBarcodeRequest {

    private Long transferId;

    private String barcode;

    private Integer quantity;

    private String scanType;
}