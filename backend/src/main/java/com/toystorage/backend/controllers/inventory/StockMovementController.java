package com.toystorage.backend.controllers.inventory;

import com.toystorage.backend.enums.inventory.MovementType;
import com.toystorage.backend.models.inventory.StockMovement;
import com.toystorage.backend.services.inventory.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * API lịch sử biến động kho
 */
@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    /*
     * Xem toàn bộ lịch sử biến động
     *
     * GET /api/stock-movements
     */
    @GetMapping
    public ResponseEntity<?> getAllMovements() {
        return ResponseEntity.ok(
                stockMovementService.getAllMovements()
        );
    }

    /*
     * Xem chi tiết movement theo id
     *
     * GET /api/stock-movements/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovementById(@PathVariable Long id) {
        return ResponseEntity.ok(
                stockMovementService.getMovementById(id)
        );
    }

    /*
     * Xem movement theo kho
     *
     * GET /api/stock-movements/warehouse/{warehouseId}
     */
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<?> getMovementsByWarehouse(
            @PathVariable Long warehouseId
    ) {
        return ResponseEntity.ok(
                stockMovementService.getMovementsByWarehouse(warehouseId)
        );
    }

    /*
     * Xem movement theo sản phẩm
     *
     * GET /api/stock-movements/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getMovementsByProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                stockMovementService.getMovementsByProduct(productId)
        );
    }

    /*
     * Xem movement theo loại
     *
     * GET /api/stock-movements/type/IN
     */
    @GetMapping("/type/{movementType}")
    public ResponseEntity<?> getMovementsByType(
            @PathVariable MovementType movementType
    ) {
        return ResponseEntity.ok(
                stockMovementService.getMovementsByType(movementType)
        );
    }

    /*
     * Tạo mới movement
     *
     * POST /api/stock-movements
     */
    @PostMapping
    public ResponseEntity<?> createMovement(
            @RequestBody StockMovement stockMovement
    ) {
        return ResponseEntity.ok(
                stockMovementService.createMovement(stockMovement)
        );
    }

    /*
     * Xóa movement
     *
     * DELETE /api/stock-movements/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovement(@PathVariable Long id) {
        stockMovementService.deleteMovement(id);

        return ResponseEntity.ok("Delete stock movement successfully");
    }
}