package com.toystorage.backend.controllers.manifests;

import com.toystorage.backend.enums.manifests.ManifestStatus;
import com.toystorage.backend.dto.response.manifests.ShipmentManifestResponse;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.repository.manifests.ShipmentManifestRepository;
import com.toystorage.backend.services.manifests.ShipmentManifestService;
import com.toystorage.backend.mapper.manifests.ShipmentManifestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/manifests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ShipmentManifestController {

    private final ShipmentManifestService shipmentManifestService;
    private final ShipmentManifestRepository shipmentManifestRepository;
    @PostMapping
    public ResponseEntity<?> createManifest(
            @RequestBody Map<String, Object> payload
    ) {
        if (payload == null) {
            throw new BadRequest("PAYLOAD_REQUIRED");
        }

        Long fromWarehouseId = Long.valueOf(payload.get("fromWarehouseId").toString());
        Long toWarehouseId = Long.valueOf(payload.get("toWarehouseId").toString());
        Long createdById = Long.valueOf(payload.get("createdById").toString());

        return ResponseEntity.ok(
                shipmentManifestService.createManifest(
                        fromWarehouseId,
                        toWarehouseId,
                        createdById
                )
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll() {

        List<ShipmentManifestResponse> responses =
                shipmentManifestRepository.findAll()
                        .stream()
                        .map(ShipmentManifestMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getManifestById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getManifestById(id)
        );
    }

    @GetMapping("/code/{manifestCode}")
    public ResponseEntity<?> getManifestByCode(
            @PathVariable String manifestCode
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getManifestByCode(manifestCode)
        );
    }

    @GetMapping("/from-warehouse/{warehouseId}")
    public ResponseEntity<?> getManifestsByFromWarehouse(
            @PathVariable Long warehouseId
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getManifestsByFromWarehouse(warehouseId)
        );
    }

    @GetMapping("/to-warehouse/{warehouseId}")
    public ResponseEntity<?> getManifestsByToWarehouse(
            @PathVariable Long warehouseId
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getManifestsByToWarehouse(warehouseId)
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

    @PostMapping("/{manifestId}/packages/{packageId}")
    public ResponseEntity<?> addPackageToManifest(
            @PathVariable Long manifestId,
            @PathVariable Long packageId
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.addPackageToManifest(
                        manifestId,
                        packageId
                )
        );
    }

    @GetMapping("/{manifestId}/packages")
    public ResponseEntity<?> getPackagesByManifest(
            @PathVariable Long manifestId
    ) {
        return ResponseEntity.ok(
                shipmentManifestService.getPackagesByManifest(manifestId)
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

        return ResponseEntity.ok("REMOVE_PACKAGE_FROM_MANIFEST_SUCCESS");
    }
}