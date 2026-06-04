package com.toystorage.backend.repository.warehouses;

import com.toystorage.backend.models.warehouses.WarehouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long> {
    
    // Tìm toàn bộ các ô kệ thuộc về một kho cụ thể
    List<WarehouseLocation> findByWarehouseId(Long warehouseId);

    // Kiểm tra trùng lặp mã vị trí hoặc mã vạch kệ
    boolean existsByLocationCode(String locationCode);
    boolean existsByLocationBarcode(String locationBarcode);
}