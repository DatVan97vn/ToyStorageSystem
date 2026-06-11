package com.toystorage.backend.controllers.transfers;

import com.toystorage.backend.dto.request.transfers.CreateTransferRequest;
import com.toystorage.backend.exceptions.BadRequest;
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

        if (request == null) {
            throw new BadRequest("TRANSFER_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                stockTransferService.createTransfer(request)
        );
    }

    /*
     * Danh sách phiếu điều kho
     */
    @GetMapping
    public ResponseEntity<?> getAllTransfers() {

        return ResponseEntity.ok(
                stockTransferService.getAllTransfers()
        );
    }

    /*
     * Chi tiết phiếu điều kho
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransferById(
            @PathVariable Long id
    ) {

        if (id == null) {
            throw new BadRequest("TRANSFER_ID_REQUIRED");
        }

        Object transfer = stockTransferService.getTransferById(id);

        if (transfer == null) {
            throw new BadRequest("TRANSFER_NOT_FOUND");
        }

        return ResponseEntity.ok(transfer);
    }

    /*
     * Approve phiếu điều kho
     */
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

    /*
     * Hoàn tất phiếu điều kho
     */
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