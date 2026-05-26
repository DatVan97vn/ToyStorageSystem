package com.toystorage.backend.services.transfers;

import com.toystorage.backend.dto.request.transfers.ScanBarcodeRequest;
import com.toystorage.backend.models.transfers.TransferScans;
import com.toystorage.backend.repository.transfers.TransferScanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/*
 * Service scan barcode
 */

@Service

@RequiredArgsConstructor

public class TransferScanService {

    private final TransferScanRepository transferScanRepository;

    /*
     * Scan barcode
     */
    public TransferScans scanBarcode(
            ScanBarcodeRequest request
    ) {

        TransferScans scan =
                TransferScans.builder()

                        .barcode(request.getBarcode())

                        .quantity(request.getQuantity())

                        .scannedAt(LocalDateTime.now())

                        .build();

        return transferScanRepository.save(scan);
    }
}