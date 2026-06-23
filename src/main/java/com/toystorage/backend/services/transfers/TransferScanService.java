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

        String barcode = request.getBarcode().trim();

        StockTransferItems transferItem =
                stockTransferItemRepository
                        .findByTransfer_IdAndProduct_Barcode(
                                request.getTransferId(),
                                barcode
                        )
                        .orElseThrow(() ->
                                new BadRequest("PRODUCT_NOT_IN_TRANSFER")
                        );

        ScanType scanType =
                request.getScanType() == null
                        ? ScanType.PICK
                        : ScanType.valueOf(request.getScanType());

        Integer quantity = request.getQuantity();

        if (scanType == ScanType.PICK) {
            Integer currentPicked =
                    transferItem.getPickedQuantity() == null
                            ? 0
                            : transferItem.getPickedQuantity();

            Integer requested =
                    transferItem.getRequestedQuantity() == null
                            ? 0
                            : transferItem.getRequestedQuantity();

            if (currentPicked + quantity > requested) {
                throw new BadRequest("PICKED_QUANTITY_EXCEED_REQUESTED");
            }

            transferItem.setPickedQuantity(
                    currentPicked + quantity
            );
        }

        if (scanType == ScanType.RECEIVE) {
            Integer currentReceived =
                    transferItem.getReceivedQuantity() == null
                            ? 0
                            : transferItem.getReceivedQuantity();

            Integer requested =
                    transferItem.getRequestedQuantity() == null
                            ? 0
                            : transferItem.getRequestedQuantity();

            if (currentReceived + quantity > requested) {
                throw new BadRequest("RECEIVED_QUANTITY_EXCEED_REQUESTED");
            }

            transferItem.setReceivedQuantity(
                    currentReceived + quantity
            );
        }

        stockTransferItemRepository.save(transferItem);

        TransferScans scan =
                TransferScans.builder()
                        .transferItem(transferItem)
                        .barcode(barcode)
                        .quantity(quantity)
                        .scanType(scanType)
                        .build();

        TransferScans saved =
                transferScanRepository.save(scan);

        return TransferScanMapper.toResponse(saved);
    }
}