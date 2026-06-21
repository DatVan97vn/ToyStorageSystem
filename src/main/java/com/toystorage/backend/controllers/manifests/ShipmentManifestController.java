package com.toystorage.backend.controllers.manifests;

import com.toystorage.backend.dto.response.manifests.ShipmentManifestResponse;
import com.toystorage.backend.enums.manifests.ManifestStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.manifests.ShipmentManifestMapper;
import com.toystorage.backend.models.manifests.ShipmentManifest;
import com.toystorage.backend.services.manifests.ShipmentManifestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manifests")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
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

        ShipmentManifest manifest =
                shipmentManifestService.createManifest(
                        fromWarehouseId,
                        toWarehouseId,
                        createdById
                );

        return ResponseEntity.ok(
                ShipmentManifestMapper.toResponse(manifest)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ShipmentManifestResponse> responses =
                shipmentManifestService.getAllManifests()
                        .stream()
                        .map(ShipmentManifestMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getManifestById(
            @PathVariable Long id
    ) {
        ShipmentManifest manifest =
                shipmentManifestService.getManifestById(id);

        return ResponseEntity.ok(
                ShipmentManifestMapper.toResponse(manifest)
        );
    }

    @GetMapping("/code/{manifestCode}")
    public ResponseEntity<?> getManifestByCode(
            @PathVariable String manifestCode
    ) {
        ShipmentManifest manifest =
                shipmentManifestService.getManifestByCode(manifestCode);

        return ResponseEntity.ok(
                ShipmentManifestMapper.toResponse(manifest)
        );
    }

    @GetMapping("/from-warehouse/{warehouseId}")
    public ResponseEntity<?> getManifestsByFromWarehouse(
            @PathVariable Long warehouseId
    ) {
        List<ShipmentManifestResponse> responses =
                shipmentManifestService
                        .getManifestsByFromWarehouse(warehouseId)
                        .stream()
                        .map(ShipmentManifestMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/to-warehouse/{warehouseId}")
    public ResponseEntity<?> getManifestsByToWarehouse(
            @PathVariable Long warehouseId
    ) {
        List<ShipmentManifestResponse> responses =
                shipmentManifestService
                        .getManifestsByToWarehouse(warehouseId)
                        .stream()
                        .map(ShipmentManifestMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
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
        shipmentManifestService.addPackageToManifest(
                manifestId,
                packageId
        );

        ShipmentManifest manifest =
                shipmentManifestService.getManifestById(manifestId);

        return ResponseEntity.ok(
                ShipmentManifestMapper.toResponse(manifest)
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