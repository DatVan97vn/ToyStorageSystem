package com.toystorage.backend.controllers.inventory;

import com.toystorage.backend.dto.request.inventory.CreateInventoryPolicyRequest;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.inventory.InventoryPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-policies")
@RequiredArgsConstructor
public class InventoryPolicyController {

    private final InventoryPolicyService inventoryPolicyService;

    @PostMapping
    public ResponseEntity<?> createPolicy(
            @RequestBody CreateInventoryPolicyRequest request
    ) {
        if (request == null) {
            throw new BadRequest("INVENTORY_POLICY_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                inventoryPolicyService.createPolicy(request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllPolicies() {
        return ResponseEntity.ok(
                inventoryPolicyService.getAllPolicies()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPolicyById(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("INVENTORY_POLICY_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                inventoryPolicyService.getPolicyById(id)
        );
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<?> getPoliciesByWarehouse(
            @PathVariable Long warehouseId
    ) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                inventoryPolicyService.getPoliciesByWarehouse(warehouseId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePolicy(
            @PathVariable Long id,
            @RequestBody CreateInventoryPolicyRequest request
    ) {
        if (id == null) {
            throw new BadRequest("INVENTORY_POLICY_ID_REQUIRED");
        }

        if (request == null) {
            throw new BadRequest("INVENTORY_POLICY_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                inventoryPolicyService.updatePolicy(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePolicy(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("INVENTORY_POLICY_ID_REQUIRED");
        }

        inventoryPolicyService.deletePolicy(id);

        return ResponseEntity.ok("DELETE_INVENTORY_POLICY_SUCCESS");
    }
}