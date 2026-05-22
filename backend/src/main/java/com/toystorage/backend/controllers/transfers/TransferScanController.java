package com.toystorage.backend.controllers.transfers;

import com.toystorage.backend.dto.request.transfers.ScanBarcodeRequest;
import com.toystorage.backend.services.transfers.TransferScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * API scan barcode
 */

@RestController

@RequestMapping("/api/scans")

@RequiredArgsConstructor

public class TransferScanController {

    private final TransferScanService transferScanService;

    /*
     * Scan barcode
     */
    @PostMapping

    public ResponseEntity<?> scanBarcode(
            @RequestBody ScanBarcodeRequest request
    ) {

        return ResponseEntity.ok(
                transferScanService.scanBarcode(request)
        );
    }
}