package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.models.inventory.InventoryBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Repository tồn kho
 */

public interface InventoryBalanceRepository
        extends JpaRepository<InventoryBalance, Long> {

    /*
     * Tìm tồn kho theo kho
     */
    List<InventoryBalance> findByWarehouseId(
            Long warehouseId
    );

    /*
     * Tìm tồn kho theo sản phẩm
     */
    List<InventoryBalance> findByProductId(
            Long productId
    );
}