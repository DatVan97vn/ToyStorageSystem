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
                .findByWarehouse_Id(warehouseId);
    }

    /*
     * Tồn theo sản phẩm
     */
    public List<InventoryBalance> getInventoryByProduct(
            Long productId
    ) {

        return inventoryBalanceRepository
                .findByProduct_Id(productId);
    }
    public InventoryBalance getInventoryById(Long id) {

        return inventoryBalanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inventory balance not found")
                );
    }

    public InventoryBalance createInventory(InventoryBalance inventoryBalance) {

        return inventoryBalanceRepository.save(inventoryBalance);
    }

    public InventoryBalance updateInventory(Long id, InventoryBalance request) {

        InventoryBalance inventoryBalance = getInventoryById(id);

        inventoryBalance.setWarehouse(request.getWarehouse());

        // Tạm thời tắt location vì bảng inventory_balances trong MySQL chưa có cột location_id
        // inventoryBalance.setLocation(request.getLocation());

        inventoryBalance.setProduct(request.getProduct());
        inventoryBalance.setQuantity(request.getQuantity());

        return inventoryBalanceRepository.save(inventoryBalance);
    }

    public void deleteInventory(Long id) {

        InventoryBalance inventoryBalance = getInventoryById(id);

        inventoryBalanceRepository.delete(inventoryBalance);
    }
}