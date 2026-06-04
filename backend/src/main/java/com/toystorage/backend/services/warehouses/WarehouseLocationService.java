package com.toystorage.backend.services.warehouses;

import com.toystorage.backend.models.warehouses.WarehouseLocation;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.warehouses.WarehouseLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseLocationService {

    private final WarehouseLocationRepository warehouseLocationRepository;

    // 1. Lấy tất cả vị trí kệ của một kho
    public List<WarehouseLocation> getLocationsByWarehouse(Long warehouseId) {
        return warehouseLocationRepository.findByWarehouseId(warehouseId);
    }

    // 2. Thêm mới một vị trí ô kệ vào kho
    @Transactional
    public WarehouseLocation createLocation(Warehouses warehouse, String zone, String aisle, String shelf, String level, String bin) {
        
        // Tự động sinh mã vị trí chuẩn hóa. Ví dụ: WH001-A-01-02-L3-B05
        String generatedCode = String.format("%s-%s-%s-%s-%s-%s", 
                warehouse.getCode(), zone, aisle, shelf, level, bin).toUpperCase().replaceAll("\\s+", "");

        if (warehouseLocationRepository.existsByLocationCode(generatedCode)) {
            throw new RuntimeException("Vị trí " + generatedCode + " này đã tồn tại trong hệ thống rồi!");
        }

        WarehouseLocation location = WarehouseLocation.builder()
                .warehouse(warehouse)
                .zone(zone.toUpperCase())
                .aisle(aisle)
                .shelf(shelf)
                .levelCode(level.toUpperCase())
                .binCode(bin)
                .locationCode(generatedCode)
                .locationBarcode(generatedCode)
                .build();

        return warehouseLocationRepository.save(location);
    }
}