package com.toystorage.backend.controllers.transfers;

import com.toystorage.backend.dto.request.transfers.CreateTransferRequest;
import com.toystorage.backend.services.transfers.StockTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * API phiếu điều kho
 */

@RestController

@RequestMapping("/api/transfers")

@RequiredArgsConstructor

public class StockTransferController {

    private final StockTransferService stockTransferService;

    /*
     * Tạo phiếu điều kho
     */
    @PostMapping

    public ResponseEntity<?> createTransfer(
            @RequestBody CreateTransferRequest request
    ) {

        return ResponseEntity.ok(
                stockTransferService.createTransfer(request)
        );
    }

    /*
     * Danh sách phiếu
     */
    @GetMapping

    public ResponseEntity<?> getAllTransfers() {

        return ResponseEntity.ok(
                stockTransferService.getAllTransfers()
        );
    }

    /*
     * Chi tiết phiếu
     */
    @GetMapping("/{id}")

    public ResponseEntity<?> getTransferById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                stockTransferService.getTransferById(id)
        );
    }

    /*
     * Approve phiếu
     */
    @PutMapping("/{id}/approve")

    public ResponseEntity<?> approveTransfer(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                stockTransferService.approveTransfer(id)
        );
    }

    /*
     * Hoàn tất phiếu
     */
    @PutMapping("/{id}/complete")

    public ResponseEntity<?> completeTransfer(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                stockTransferService.completeTransfer(id)
        );
    }
}