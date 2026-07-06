package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.models.inventory.InventoryPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryPolicyRepository extends JpaRepository<InventoryPolicy, Long> {

    List<InventoryPolicy> findByWarehouse_Id(Long warehouseId);

    List<InventoryPolicy> findByProduct_Id(Long productId);

    Optional<InventoryPolicy> findByWarehouse_IdAndProduct_Id(
            Long warehouseId,
            Long productId
    );
}