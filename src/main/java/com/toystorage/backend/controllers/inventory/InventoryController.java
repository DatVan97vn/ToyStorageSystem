package com.toystorage.backend.controllers.inventory;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.inventory.InventoryMapper;
import com.toystorage.backend.models.inventory.InventoryBalance;
import com.toystorage.backend.services.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryMapper inventoryMapper;

    /*
     * Tạo mới tồn kho
     * POST /api/inventory/create
     */
    @PostMapping("/create")
    public ResponseEntity<?> createInventory(
            @RequestBody InventoryBalance inventoryBalance
    ) {
        if (inventoryBalance == null) {
            throw new BadRequest("INVENTORY_REQUIRED");
        }

        InventoryBalance created = inventoryService.createInventory(inventoryBalance);

        return ResponseEntity.ok(
                inventoryMapper.toResponse(created)
        );
    }

    /*
     * Cập nhật tồn kho
     * PUT /api/inventory/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(
            @PathVariable Long id,
            @RequestBody InventoryBalance inventoryBalance
    ) {
        if (id == null) {
            throw new BadRequest("INVENTORY_ID_REQUIRED");
        }

        if (inventoryBalance == null) {
            throw new BadRequest("INVENTORY_REQUIRED");
        }

        InventoryBalance updated = inventoryService.updateInventory(id, inventoryBalance);

        return ResponseEntity.ok(
                inventoryMapper.toResponse(updated)
        );
    }

    /*
     * Xóa tồn kho
     * DELETE /api/inventory/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("INVENTORY_ID_REQUIRED");
        }

        inventoryService.deleteInventory(id);

        return ResponseEntity.ok("DELETE_INVENTORY_SUCCESS");
    }

    /*
     * Xem toàn bộ tồn kho
     * GET /api/inventory
     */
    @GetMapping
    public ResponseEntity<?> getAllInventory() {
        return ResponseEntity.ok(
                inventoryService.getAllInventory()
                        .stream()
                        .map(inventoryMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }

    /*
     * Xem tồn theo kho
     * GET /api/inventory/warehouse/{warehouseId}
     */
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<?> getInventoryByWarehouse(
            @PathVariable Long warehouseId
    ) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                inventoryService.getInventoryByWarehouse(warehouseId)
                        .stream()
                        .map(inventoryMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }

    /*
     * Xem tồn theo sản phẩm
     * GET /api/inventory/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getInventoryByProduct(
            @PathVariable Long productId
    ) {
        if (productId == null) {
            throw new BadRequest("PRODUCT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                inventoryService.getInventoryByProduct(productId)
                        .stream()
                        .map(inventoryMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }

    /*
     * Xem chi tiết tồn kho theo id
     * GET /api/inventory/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("INVENTORY_ID_REQUIRED");
        }

        InventoryBalance inventoryBalance = inventoryService.getInventoryById(id);

        if (inventoryBalance == null) {
            throw new BadRequest("INVENTORY_NOT_FOUND");
        }

        return ResponseEntity.ok(
                inventoryMapper.toResponse(inventoryBalance)
        );
    }
}