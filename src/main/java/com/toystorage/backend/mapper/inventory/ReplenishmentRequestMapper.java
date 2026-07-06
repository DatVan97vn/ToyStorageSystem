package com.toystorage.backend.mapper.inventory;

import com.toystorage.backend.dto.response.inventory.ReplenishmentRequestResponse;
import com.toystorage.backend.models.inventory.ReplenishmentRequest;
import org.springframework.stereotype.Component;

@Component
public class ReplenishmentRequestMapper {

    public ReplenishmentRequestResponse toResponse(ReplenishmentRequest request) {
        if (request == null) {
            return null;
        }

        return ReplenishmentRequestResponse.builder()
                .id(request.getId())
                .requestCode(request.getRequestCode())
                .requestWarehouseId(request.getRequestWarehouse() != null ? request.getRequestWarehouse().getId() : null)
                .requestWarehouseName(request.getRequestWarehouse() != null ? request.getRequestWarehouse().getName() : null)
                .sourceWarehouseId(request.getSourceWarehouse() != null ? request.getSourceWarehouse().getId() : null)
                .sourceWarehouseName(request.getSourceWarehouse() != null ? request.getSourceWarehouse().getName() : null)
                .productId(request.getProduct() != null ? request.getProduct().getId() : null)
                .productName(request.getProduct() != null ? request.getProduct().getName() : null)
                .productSku(request.getProduct() != null ? request.getProduct().getSku() : null)
                .currentQuantity(request.getCurrentQuantity())
                .requestedQuantity(request.getRequestedQuantity())
                .approvedQuantity(request.getApprovedQuantity())
                .requestedById(request.getRequestedBy() != null ? request.getRequestedBy().getId() : null)
                .requestedByName(request.getRequestedBy() != null ? request.getRequestedBy().getName() : null)
                .approvedById(request.getApprovedBy() != null ? request.getApprovedBy().getId() : null)
                .approvedByName(request.getApprovedBy() != null ? request.getApprovedBy().getName() : null)
                .stockTransferId(request.getStockTransfer() != null ? request.getStockTransfer().getId() : null)
                .note(request.getNote())
                .status(request.getStatus() != null ? request.getStatus().name() : null)
                .createdAt(request.getCreatedAt())
                .approvedAt(request.getApprovedAt())
                .completedAt(request.getCompletedAt())
                .build();
    }
}