package com.toystorage.backend.controllers.inventory;

import com.toystorage.backend.dto.request.inventory.ApproveReplenishmentRequest;
import com.toystorage.backend.dto.request.inventory.CreateReplenishmentRequest;
import com.toystorage.backend.enums.inventory.ReplenishmentStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.inventory.ReplenishmentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/replenishment-requests")
@RequiredArgsConstructor
public class ReplenishmentRequestController {

    private final ReplenishmentRequestService replenishmentRequestService;

    @PostMapping
    public ResponseEntity<?> createRequest(
            @RequestBody CreateReplenishmentRequest request
    ) {
        if (request == null) {
            throw new BadRequest("REPLENISHMENT_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                replenishmentRequestService.createRequest(request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(
                replenishmentRequestService.getAllRequests()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("REPLENISHMENT_REQUEST_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                replenishmentRequestService.getRequestById(id)
        );
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<?> getRequestsByWarehouse(
            @PathVariable Long warehouseId
    ) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                replenishmentRequestService.getRequestsByWarehouse(warehouseId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getRequestsByStatus(
            @PathVariable ReplenishmentStatus status
    ) {
        if (status == null) {
            throw new BadRequest("REPLENISHMENT_STATUS_REQUIRED");
        }

        return ResponseEntity.ok(
                replenishmentRequestService.getRequestsByStatus(status)
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable Long id,
            @RequestBody ApproveReplenishmentRequest request
    ) {
        if (id == null) {
            throw new BadRequest("REPLENISHMENT_REQUEST_ID_REQUIRED");
        }

        if (request == null) {
            throw new BadRequest("APPROVE_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                replenishmentRequestService.approveRequest(id, request)
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        if (id == null) {
            throw new BadRequest("REPLENISHMENT_REQUEST_ID_REQUIRED");
        }

        String note = payload != null ? payload.get("note") : null;

        return ResponseEntity.ok(
                replenishmentRequestService.rejectRequest(id, note)
        );
    }

    @PutMapping("/{id}/transfer-created")
    public ResponseEntity<?> markTransferCreated(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("REPLENISHMENT_REQUEST_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                replenishmentRequestService.markTransferCreated(id)
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeRequest(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("REPLENISHMENT_REQUEST_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                replenishmentRequestService.completeRequest(id)
        );
    }
}