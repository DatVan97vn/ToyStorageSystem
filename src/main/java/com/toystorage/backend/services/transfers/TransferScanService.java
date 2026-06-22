package com.toystorage.backend.services.transfers;

import com.toystorage.backend.dto.request.transfers.ScanBarcodeRequest;
import com.toystorage.backend.dto.response.transfers.TransferScanResponse;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.transfers.TransferScanMapper;
import com.toystorage.backend.models.transfers.TransferScans;
import com.toystorage.backend.repository.transfers.TransferScanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferScanService {

    private final TransferScanRepository transferScanRepository;

    @Transactional
    public TransferScanResponse scanBarcode(
            ScanBarcodeRequest request
    ) {
        if (request == null) {
            throw new BadRequest("SCAN_REQUEST_REQUIRED");
        }

        if (request.getBarcode() == null || request.getBarcode().isBlank()) {
            throw new BadRequest("BARCODE_REQUIRED");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequest("QUANTITY_INVALID");
        }

        TransferScans scan =
                TransferScans.builder()
                        .barcode(request.getBarcode())
                        .quantity(request.getQuantity())
                        .scannedAt(LocalDateTime.now())
                        .build();

        TransferScans saved =
                transferScanRepository.save(scan);

        return TransferScanMapper.toResponse(saved);
    }
}