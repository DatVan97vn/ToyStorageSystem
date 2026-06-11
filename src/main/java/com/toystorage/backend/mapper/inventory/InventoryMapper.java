package com.toystorage.backend.mapper.inventory;

import com.toystorage.backend.dto.response.inventory.InventoryResponse;
import com.toystorage.backend.models.inventory.InventoryBalance;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryResponse toResponse(InventoryBalance inventoryBalance) {
        if (inventoryBalance == null) {
            return null;
        }

        InventoryResponse response = new InventoryResponse();

        response.setId(inventoryBalance.getId());
        response.setQuantity(inventoryBalance.getQuantity());

        if (inventoryBalance.getProduct() != null) {
            response.setProductId(inventoryBalance.getProduct().getId());
            response.setProductName(inventoryBalance.getProduct().getName());
        }

        if (inventoryBalance.getWarehouse() != null) {
            response.setWarehouseId(inventoryBalance.getWarehouse().getId());
            response.setWarehouseName(inventoryBalance.getWarehouse().getName());
        }

        return response;
    }
}