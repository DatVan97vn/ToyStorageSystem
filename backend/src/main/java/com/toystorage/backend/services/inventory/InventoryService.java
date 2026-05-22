package com.toystorage.backend.services.inventory;

import com.toystorage.backend.models.inventory.InventoryBalance;
import com.toystorage.backend.repository.inventory.InventoryBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service tồn kho
 */

@Service

@RequiredArgsConstructor

public class InventoryService {

    private final InventoryBalanceRepository inventoryBalanceRepository;

    /*
     * Toàn bộ tồn kho
     */
    public List<InventoryBalance> getAllInventory() {

        return inventoryBalanceRepository.findAll();
    }

    /*
     * Tồn theo kho
     */
    public List<InventoryBalance> getInventoryByWarehouse(
            Long warehouseId
    ) {

        return inventoryBalanceRepository
                .findByWarehouseId(warehouseId);
    }

    /*
     * Tồn theo sản phẩm
     */
    public List<InventoryBalance> getInventoryByProduct(
            Long productId
    ) {

        return inventoryBalanceRepository
                .findByProductId(productId);
    }
}