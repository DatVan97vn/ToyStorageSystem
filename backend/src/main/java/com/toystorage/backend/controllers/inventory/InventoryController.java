package com.toystorage.backend.controllers.inventory;

import com.toystorage.backend.services.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/inventory")

@RequiredArgsConstructor

public class InventoryController {
    private final InventoryService inventoryService;

    /*
     * Xem toàn bộ tồn kho
     */
    @GetMapping

    public ResponseEntity<?> getAllInventory() {

        return ResponseEntity.ok(
                inventoryService.getAllInventory()
        );
    }

    /*
     * Xem tồn theo kho
     */
    @GetMapping("/warehouse/{warehouseId}")

    public ResponseEntity<?> getInventoryByWarehouse(
            @PathVariable Long warehouseId
    ) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByWarehouse(warehouseId)
        );
    }

    /*
     * Xem tồn theo sản phẩm
     */
    @GetMapping("/product/{productId}")

    public ResponseEntity<?> getInventoryByProduct(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByProduct(productId)
        );
    }

}
