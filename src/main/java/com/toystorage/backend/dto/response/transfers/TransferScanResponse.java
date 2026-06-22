package com.toystorage.backend.dto.response.transfers;

import com.toystorage.backend.enums.transfers.ScanType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransferScanResponse {

    private Long id;

    private Long transferItemId;

    private String barcode;

    private Integer quantity;

    private ScanType scanType;

    private LocalDateTime scannedAt;
}