package com.toystorage.backend.controllers.warehouses;

import com.toystorage.backend.dto.response.warehouses.WarehouseResponse;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.services.warehouses.WarehouseLocationService;
import com.toystorage.backend.services.warehouses.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
 * API quản lý kho
 */
@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final WarehouseLocationService warehouseLocationService;

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
        if (id == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        Object warehouse = warehouseService.getWarehouseById(id);

        if (warehouse == null) {
            throw new BadRequest("WAREHOUSE_NOT_FOUND");
        }

        return ResponseEntity.ok(warehouse);
    }

    /*
     * Tìm kho theo code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getWarehouseByCode(
            @PathVariable String code
    ) {
        if (code == null || code.isBlank()) {
            throw new BadRequest("WAREHOUSE_CODE_REQUIRED");
        }

        Object warehouse = warehouseService.getWarehouseByCode(code);

        if (warehouse == null) {
            throw new BadRequest("WAREHOUSE_NOT_FOUND");
        }

        return ResponseEntity.ok(warehouse);
    }

    /*
     * Tạo kho
     */
    @PostMapping
    public ResponseEntity<?> createWarehouse(
            @RequestBody Warehouses warehouse
    ) {
        if (warehouse == null) {
            throw new BadRequest("WAREHOUSE_REQUIRED");
        }

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
        if (id == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        if (warehouse == null) {
            throw new BadRequest("WAREHOUSE_REQUIRED");
        }

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
        if (id == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        warehouseService.deleteWarehouse(id);

        return ResponseEntity.ok(
                "DELETE_WAREHOUSE_SUCCESS"
        );
    }

    // =========================================================================
    // VỊ TRÍ Ô KỆ TRONG KHO
    // =========================================================================

    /*
     * Lấy danh sách toàn bộ vị trí ô kệ thuộc về kho này
     */
    @GetMapping("/{id}/locations")
    public ResponseEntity<?> getWarehouseLocations(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                warehouseLocationService.getLocationsByWarehouse(id)
        );
    }

    /*
     * Tạo nhanh một vị trí ô kệ trong kho
     */
    @PostMapping("/{id}/locations")
    public ResponseEntity<?> createLocation(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        if (id == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        if (payload == null) {
            throw new BadRequest("LOCATION_PAYLOAD_REQUIRED");
        }

        WarehouseResponse warehouseRes = warehouseService.getWarehouseById(id);

        if (warehouseRes == null) {
            throw new BadRequest("WAREHOUSE_NOT_FOUND");
        }

        String zone = payload.get("zone");
        String aisle = payload.get("aisle");
        String shelf = payload.get("shelf");
        String level = payload.get("levelCode");
        String bin = payload.get("binCode");

        if (zone == null || zone.isBlank()) {
            throw new BadRequest("ZONE_REQUIRED");
        }

        if (aisle == null || aisle.isBlank()) {
            throw new BadRequest("AISLE_REQUIRED");
        }

        if (shelf == null || shelf.isBlank()) {
            throw new BadRequest("SHELF_REQUIRED");
        }

        if (level == null || level.isBlank()) {
            throw new BadRequest("LEVEL_CODE_REQUIRED");
        }

        if (bin == null || bin.isBlank()) {
            throw new BadRequest("BIN_CODE_REQUIRED");
        }

        Warehouses warehouseEntity = new Warehouses();
        warehouseEntity.setId(warehouseRes.getId());
        warehouseEntity.setCode(warehouseRes.getCode());

        Object newLocation = warehouseLocationService.createLocation(
                warehouseEntity,
                zone.trim(),
                aisle.trim(),
                shelf.trim(),
                level.trim(),
                bin.trim()
        );

        if (newLocation == null) {
            throw new BadRequest("LOCATION_CREATE_FAILED");
        }

        return ResponseEntity.ok(newLocation);
    }
}