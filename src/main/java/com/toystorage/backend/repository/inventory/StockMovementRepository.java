package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.enums.inventory.MovementType;
import com.toystorage.backend.models.inventory.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    /*
     * Theo kho
     */
    List<StockMovement> findByWarehouse_Id(Long warehouseId);

    /*
     * Theo sản phẩm
     */
    List<StockMovement> findByProduct_Id(Long productId);

    /*
     * Theo loại movement
     */
    List<StockMovement> findByMovementType(
            MovementType movementType
    );
}