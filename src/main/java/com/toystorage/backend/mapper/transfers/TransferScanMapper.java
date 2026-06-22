package com.toystorage.backend.mapper.transfers;

import com.toystorage.backend.dto.response.transfers.TransferScanResponse;
import com.toystorage.backend.models.transfers.TransferScans;

public class TransferScanMapper {

    public static TransferScanResponse toResponse(
            TransferScans scan
    ) {
        if (scan == null) {
            return null;
        }

        return TransferScanResponse.builder()
                .id(scan.getId())

                .transferItemId(
                        scan.getTransferItem() != null
                                ? scan.getTransferItem().getId()
                                : null
                )

                .barcode(scan.getBarcode())
                .quantity(scan.getQuantity())
                .scanType(scan.getScanType())
                .scannedAt(scan.getScannedAt())
                .build();
    }
}