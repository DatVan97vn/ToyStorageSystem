package com.toystorage.backend.controllers.warehouses;

import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.services.warehouses.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * API quản lý kho
 */

@RestController

@RequestMapping("/api/warehouses")

@RequiredArgsConstructor

public class WarehouseController {

    private final WarehouseService warehouseService;

    /*
     * Danh sách kho
     */
    @GetMapping

    public ResponseEntity<?> getAllWarehouses() {

        return ResponseEntity.ok(
                warehouseService.getAllWarehouses()
        );
    }

    /*
     * Chi tiết kho
     */
    @GetMapping("/{id}")

    public ResponseEntity<?> getWarehouseById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                warehouseService.getWarehouseById(id)
        );
    }
    /*
     * Tìm kho theo code
     */
    @GetMapping("/code/{code}")

    public ResponseEntity<?> getWarehouseByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                warehouseService.getWarehouseByCode(code)
        );
    }

    /*
     * Tạo kho
     */
    @PostMapping

    public ResponseEntity<?> createWarehouse(
            @RequestBody Warehouses warehouse
    ) {

        return ResponseEntity.ok(
                warehouseService.createWarehouse(warehouse)
        );
    }

    /*
     * Cập nhật kho
     */
    @PutMapping("/{id}")

    public ResponseEntity<?> updateWarehouse(
            @PathVariable Long id,
            @RequestBody Warehouses warehouse
    ) {

        return ResponseEntity.ok(
                warehouseService.updateWarehouse(id, warehouse)
        );
    }

    /*
     * Xóa kho
     */
    @DeleteMapping("/{id}")

    public ResponseEntity<?> deleteWarehouse(
            @PathVariable Long id
    ) {

        warehouseService.deleteWarehouse(id);

        return ResponseEntity.ok(
                "Delete warehouse successfully"
        );
    }
}