package com.toystorage.backend.mapper.inventory;

import com.toystorage.backend.dto.response.inventory.InventoryPolicyResponse;
import com.toystorage.backend.models.inventory.InventoryPolicy;
import org.springframework.stereotype.Component;

@Component
public class InventoryPolicyMapper {

    public InventoryPolicyResponse toResponse(InventoryPolicy policy) {
        if (policy == null) {
            return null;
        }

        return InventoryPolicyResponse.builder()
                .id(policy.getId())
                .warehouseId(policy.getWarehouse() != null ? policy.getWarehouse().getId() : null)
                .warehouseName(policy.getWarehouse() != null ? policy.getWarehouse().getName() : null)
                .productId(policy.getProduct() != null ? policy.getProduct().getId() : null)
                .productName(policy.getProduct() != null ? policy.getProduct().getName() : null)
                .productSku(policy.getProduct() != null ? policy.getProduct().getSku() : null)
                .minimumQuantity(policy.getMinimumQuantity())
                .maximumQuantity(policy.getMaximumQuantity())
                .reorderQuantity(policy.getReorderQuantity())
                .active(policy.getActive())
                .createdAt(policy.getCreatedAt())
                .updatedAt(policy.getUpdatedAt())
                .build();
    }
}