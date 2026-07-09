package com.toystorage.backend.controllers.manifests;

import com.toystorage.backend.dto.response.manifests.ShipmentManifestResponse;
import com.toystorage.backend.enums.manifests.ManifestStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.manifests.ShipmentManifestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/manifests")
@RequiredArgsConstructor
public class ShipmentManifestController {

    private final ShipmentManifestService shipmentManifestService;

    @PostMapping
    public ResponseEntity<?> createManifest(
            @RequestBody Map<String, Object> payload
    ) {
        if (payload == null) {
            throw new BadRequest("PAYLOAD_REQUIRED");
        }

        Long fromWarehouseId =
                Long.valueOf(payload.get("fromWarehouseId").toString());

        Long toWarehouseId =
                Long.valueOf(payload.get("toWarehouseId").toString());

        Long createdById =
                Long.valueOf(payload.get("createdById").toString());

        ShipmentManifestResponse response =
                shipmentManifestService.createManifest(
                        fromWarehouseId,
                        toWarehouseId,
                        createdById
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
                shipmentManifestService.getAllManifestResponses()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getManifestById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getManifestResponseById(id)
        );
    }

    @GetMapping("/code/{manifestCode}")
    public ResponseEntity<?> getManifestByCode(
            @PathVariable String manifestCode
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getManifestResponseByCode(manifestCode)
        );
    }

    @GetMapping("/from-warehouse/{warehouseId}")
    public ResponseEntity<?> getManifestsByFromWarehouse(
            @PathVariable Long warehouseId
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getManifestResponsesByFromWarehouse(
                        warehouseId
                )
        );
    }

    @GetMapping("/to-warehouse/{warehouseId}")
    public ResponseEntity<?> getManifestsByToWarehouse(
            @PathVariable Long warehouseId
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getManifestResponsesByToWarehouse(
                        warehouseId
                )
        );
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @PathVariable ManifestStatus status
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.updateStatus(id, status)
        );
    }

    // =========================================================
    // TRANSFERS
    // Business dùng phần này để gắn transfer vào manifest
    // =========================================================

    @PostMapping("/{manifestId}/transfers/{transferId}")
    public ResponseEntity<?> addTransferToManifest(
            @PathVariable Long manifestId,
            @PathVariable Long transferId
    ) {
        shipmentManifestService.addTransferToManifest(
                manifestId,
                transferId
        );

        return ResponseEntity.ok(
                "ADD_TRANSFER_TO_MANIFEST_SUCCESS"
        );
    }

    @GetMapping("/{manifestId}/transfers")
    public ResponseEntity<?> getTransfersByManifest(
            @PathVariable Long manifestId
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getTransferResponsesByManifest(
                        manifestId
                )
        );
    }

    @DeleteMapping("/{manifestId}/transfers/{transferId}")
    public ResponseEntity<?> removeTransferFromManifest(
            @PathVariable Long manifestId,
            @PathVariable Long transferId
    ) {
        shipmentManifestService.removeTransferFromManifest(
                manifestId,
                transferId
        );

        return ResponseEntity.ok(
                "REMOVE_TRANSFER_FROM_MANIFEST_SUCCESS"
        );
    }

    // =========================================================
    // PACKAGES
    // Kho đóng hàng xong mới gắn package vào manifest
    // =========================================================

    @PostMapping("/{manifestId}/packages/{packageId}")
    public ResponseEntity<?> addPackageToManifest(
            @PathVariable Long manifestId,
            @PathVariable Long packageId
    ) {
        shipmentManifestService.addPackageToManifest(
                manifestId,
                packageId
        );

        return ResponseEntity.ok(
                "ADD_PACKAGE_TO_MANIFEST_SUCCESS"
        );
    }

    @GetMapping("/{manifestId}/packages")
    public ResponseEntity<?> getPackagesByManifest(
            @PathVariable Long manifestId
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getPackageResponsesByManifest(
                        manifestId
                )
        );
    }

    @DeleteMapping("/{manifestId}/packages/{packageId}")
    public ResponseEntity<?> removePackageFromManifest(
            @PathVariable Long manifestId,
            @PathVariable Long packageId
    ) {
        shipmentManifestService.removePackageFromManifest(
                manifestId,
                packageId
        );

        return ResponseEntity.ok(
                "REMOVE_PACKAGE_FROM_MANIFEST_SUCCESS"
        );
    }
}