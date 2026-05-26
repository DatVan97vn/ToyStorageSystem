package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.models.inventory.InventoryBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
 * Repository tồn kho
 */

public interface InventoryBalanceRepository
        extends JpaRepository<InventoryBalance, Long> {

    /*
     * Tìm tồn kho theo kho
     */
    List<InventoryBalance> findByWarehouse_Id(
            Long warehouseId
    );

    /*
     * Tìm tồn kho theo sản phẩm
     */
    List<InventoryBalance> findByProduct_Id(
            Long productId
    );

    /*
     * Theo kho + sản phẩm
     */
    Optional<InventoryBalance>
    findByWarehouse_IdAndProduct_Id(
            Long warehouseId,
            Long productId
    );
}