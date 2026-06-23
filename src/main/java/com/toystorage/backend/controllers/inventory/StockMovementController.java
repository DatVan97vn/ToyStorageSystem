package com.toystorage.backend.controllers.inventory;

import com.toystorage.backend.dto.response.inventory.StockMovementResponse;
import com.toystorage.backend.enums.inventory.MovementType;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.inventory.StockMovement;
import com.toystorage.backend.services.inventory.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor

public class StockMovementController {

    private final StockMovementService stockMovementService;

    @GetMapping
    public ResponseEntity<?> getAllMovements() {
        return ResponseEntity.ok(
                stockMovementService.getAllMovements()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovementById(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("MOVEMENT_ID_REQUIRED");
        }

        StockMovementResponse movement =
                stockMovementService.getMovementById(id);

        if (movement == null) {
            throw new BadRequest("MOVEMENT_NOT_FOUND");
        }

        return ResponseEntity.ok(movement);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<?> getMovementsByWarehouse(
            @PathVariable Long warehouseId
    ) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                stockMovementService.getMovementsByWarehouse(warehouseId)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getMovementsByProduct(
            @PathVariable Long productId
    ) {
        if (productId == null) {
            throw new BadRequest("PRODUCT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                stockMovementService.getMovementsByProduct(productId)
        );
    }

    @GetMapping("/type/{movementType}")
    public ResponseEntity<?> getMovementsByType(
            @PathVariable MovementType movementType
    ) {
        if (movementType == null) {
            throw new BadRequest("MOVEMENT_TYPE_REQUIRED");
        }

        return ResponseEntity.ok(
                stockMovementService.getMovementsByType(movementType)
        );
    }

    @PostMapping
    public ResponseEntity<?> createMovement(
            @RequestBody StockMovement stockMovement
    ) {
        if (stockMovement == null) {
            throw new BadRequest("STOCK_MOVEMENT_REQUIRED");
        }

        return ResponseEntity.ok(
                stockMovementService.createMovement(stockMovement)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovement(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("MOVEMENT_ID_REQUIRED");
        }

        stockMovementService.deleteMovement(id);

        return ResponseEntity.ok("DELETE_STOCK_MOVEMENT_SUCCESS");
    }
}