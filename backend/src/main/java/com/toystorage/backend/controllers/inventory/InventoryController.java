package com.toystorage.backend.controllers.inventory;

import com.toystorage.backend.services.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.toystorage.backend.models.inventory.InventoryBalance;

@RestController

@RequestMapping("/api/inventory")

@RequiredArgsConstructor

public class InventoryController {
    private final InventoryService inventoryService;
    /*
     * Tạo mới tồn kho
     *
     * POST /api/inventory
     */
    @PostMapping("/create")

    public ResponseEntity<?> createInventory(
            @RequestBody InventoryBalance inventoryBalance
    ) {

        return ResponseEntity.ok(
                inventoryService.createInventory(inventoryBalance)
        );
    }

    /*
     * Cập nhật tồn kho
     *
     * PUT /api/inventory/{id}
     */
    @PutMapping("/{id}")

    public ResponseEntity<?> updateInventory(
            @PathVariable Long id,
            @RequestBody InventoryBalance inventoryBalance
    ) {

        return ResponseEntity.ok(
                inventoryService.updateInventory(id, inventoryBalance)
        );
    }

    /*
     * Xóa tồn kho
     *
     * DELETE /api/inventory/{id}
     */
    @DeleteMapping("/{id}")

    public ResponseEntity<?> deleteInventory(
            @PathVariable Long id
    ) {

        inventoryService.deleteInventory(id);

        return ResponseEntity.ok(
                "Delete inventory successfully"
        );
    }
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
    /*
     * Xem chi tiết tồn kho theo id
     *
     * GET /api/inventory/{id}
     */
    @GetMapping("/{id}")

    public ResponseEntity<?> getInventoryById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(id)
        );
    }



}
