package com.toystorage.backend.controllers.transfers;

import com.toystorage.backend.dto.request.transfers.ScanBarcodeRequest;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.transfers.TransferScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scans")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransferScanController {

    private final TransferScanService transferScanService;

    @PostMapping
    public ResponseEntity<?> scanBarcode(
            @RequestBody ScanBarcodeRequest request
    ) {
        if (request == null) {
            throw new BadRequest("SCAN_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                transferScanService.scanBarcode(request)
        );
    }
}