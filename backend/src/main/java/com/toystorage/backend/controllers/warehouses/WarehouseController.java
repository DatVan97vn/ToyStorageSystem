package com.toystorage.backend.controllers.warehouses;

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
}