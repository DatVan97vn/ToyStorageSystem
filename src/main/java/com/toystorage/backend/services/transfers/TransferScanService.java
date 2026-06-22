package com.toystorage.backend.services.transfers;

import com.toystorage.backend.dto.request.transfers.ScanBarcodeRequest;
import com.toystorage.backend.dto.response.transfers.TransferScanResponse;
import com.toystorage.backend.enums.transfers.ScanType;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.transfers.TransferScanMapper;
import com.toystorage.backend.models.transfers.StockTransferItems;
import com.toystorage.backend.models.transfers.TransferScans;
import com.toystorage.backend.repository.transfers.StockTransferItemRepository;
import com.toystorage.backend.repository.transfers.TransferScanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferScanService {

    private final TransferScanRepository transferScanRepository;
    private final StockTransferItemRepository stockTransferItemRepository;

    @Transactional
    public TransferScanResponse scanBarcode(
            ScanBarcodeRequest request
    ) {
        if (request == null) {
            throw new BadRequest("SCAN_REQUEST_REQUIRED");
        }

        if (request.getTransferId() == null) {
            throw new BadRequest("TRANSFER_ID_REQUIRED");
        }

        if (request.getBarcode() == null || request.getBarcode().isBlank()) {
            throw new BadRequest("BARCODE_REQUIRED");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequest("QUANTITY_INVALID");
        }

        StockTransferItems transferItem =
                stockTransferItemRepository
                        .findByTransfer_IdAndProduct_Barcode(
                                request.getTransferId(),
                                request.getBarcode()
                        )
                        .orElseThrow(() ->
                                new BadRequest("PRODUCT_NOT_IN_TRANSFER")
                        );

        ScanType scanType =
                request.getScanType() == null
                        ? ScanType.RECEIVE
                        : ScanType.valueOf(request.getScanType());

        TransferScans scan =
                TransferScans.builder()
                        .transferItem(transferItem)
                        .barcode(request.getBarcode())
                        .quantity(request.getQuantity())
                        .scanType(scanType)
                        .build();

        TransferScans saved =
                transferScanRepository.save(scan);

        return TransferScanMapper.toResponse(saved);
    }
}