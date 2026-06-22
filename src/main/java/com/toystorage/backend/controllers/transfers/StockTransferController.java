package com.toystorage.backend.controllers.transfers;

import com.toystorage.backend.dto.request.transfers.CreateTransferRequest;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.transfers.StockTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockTransferController {

    private final StockTransferService stockTransferService;

    @PostMapping
    public ResponseEntity<?> createTransfer(
            @RequestBody CreateTransferRequest request
    ) {
        if (request == null) {
            throw new BadRequest("TRANSFER_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                stockTransferService.createTransfer(request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllTransfers() {
        return ResponseEntity.ok(
                stockTransferService.getAllTransfers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransferById(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("TRANSFER_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                stockTransferService.getTransferById(id)
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveTransfer(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("TRANSFER_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                stockTransferService.approveTransfer(id)
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeTransfer(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("TRANSFER_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                stockTransferService.completeTransfer(id)
        );
    }
}