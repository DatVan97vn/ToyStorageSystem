package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.models.inventory.InventoryBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryBalanceRepository extends JpaRepository<InventoryBalance, Long> {

    List<InventoryBalance> findByWarehouse_Id(Long warehouseId);

    List<InventoryBalance> findByProduct_Id(Long productId);

    List<InventoryBalance> findByLocation_Id(Long locationId);

    Optional<InventoryBalance> findByWarehouse_IdAndProduct_Id(
            Long warehouseId,
            Long productId
    );

    Optional<InventoryBalance> findByWarehouse_IdAndLocation_IdAndProduct_Id(
            Long warehouseId,
            Long locationId,
            Long productId
    );
}