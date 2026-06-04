package com.toystorage.backend.controllers.warehouses;

import com.toystorage.backend.dto.response.warehouses.WarehouseResponse;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.services.warehouses.WarehouseService;
import com.toystorage.backend.services.warehouses.WarehouseLocationService;
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

    // =========================================================================
    // VỊ TRÍ Ô KỆ TRONG KHO (WAREHOUSE LOCATIONS)
    // =========================================================================

    /*
     * Lấy danh sách toàn bộ vị trí ô kệ thuộc về kho này
     * Đường dẫn: GET /api/warehouses/{id}/locations
     */
    @GetMapping("/{id}/locations")
    public ResponseEntity<?> getWarehouseLocations(@PathVariable Long id) {
        return ResponseEntity.ok(
                warehouseLocationService.getLocationsByWarehouse(id)
        );
    }

    /*
     * API cho IT Manager tạo nhanh một vị trí ô kệ trong kho
     * Đường dẫn: POST /api/warehouses/{id}/locations
     */
    @PostMapping("/{id}/locations")
    public ResponseEntity<?> createLocation(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        try {
            // 1. Kiểm tra kho tồn tại và lấy thông tin DTO (Fix dứt điểm lỗi orElse)
            WarehouseResponse warehouseRes = warehouseService.getWarehouseById(id);
            if (warehouseRes == null) {
                return ResponseEntity.badRequest().body("WAREHOUSE_NOT_FOUND");
            }

            // 2. Bóc tách dữ liệu cấu trúc ô kệ từ body request gửi lên
            String zone = payload.get("zone");
            String aisle = payload.get("aisle");
            String shelf = payload.get("shelf");
            String level = payload.get("levelCode");
            String bin = payload.get("binCode");

            // 3. Khởi tạo trực tiếp thực thể bằng toán tử new truyền thống 
            // Giúp tránh cảnh báo lỗi @Builder của Lombok trên các thực thể JPA
            Warehouses warehouseEntity = new Warehouses();
            warehouseEntity.setId(warehouseRes.getId());
            warehouseEntity.setCode(warehouseRes.getCode());

            // 4. Tiến hành gọi service tạo ô kệ
            var newLocation = warehouseLocationService.createLocation(
                    warehouseEntity, zone, aisle, shelf, level, bin
            );
            
            return ResponseEntity.ok(newLocation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}