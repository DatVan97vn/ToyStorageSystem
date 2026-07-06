package com.toystorage.backend.mapper.inventory;

import com.toystorage.backend.dto.response.inventory.PutawayTaskResponse;
import com.toystorage.backend.models.inventory.PutawayTask;
import org.springframework.stereotype.Component;

@Component
public class PutawayTaskMapper {

    public PutawayTaskResponse toResponse(PutawayTask task) {
        if (task == null) {
            return null;
        }

        return PutawayTaskResponse.builder()
                .id(task.getId())
                .goodsReceiptId(task.getGoodsReceipt() != null ? task.getGoodsReceipt().getId() : null)
                .goodsReceiptItemId(task.getGoodsReceiptItem() != null ? task.getGoodsReceiptItem().getId() : null)
                .warehouseId(task.getWarehouse() != null ? task.getWarehouse().getId() : null)
                .warehouseName(task.getWarehouse() != null ? task.getWarehouse().getName() : null)
                .productId(task.getProduct() != null ? task.getProduct().getId() : null)
                .productName(task.getProduct() != null ? task.getProduct().getName() : null)
                .productSku(task.getProduct() != null ? task.getProduct().getSku() : null)
                .locationId(task.getLocation() != null ? task.getLocation().getId() : null)
                .locationCode(task.getLocation() != null ? task.getLocation().getLocationCode() : null)
                .locationBarcode(task.getLocation() != null ? task.getLocation().getLocationBarcode() : null)
                .quantity(task.getQuantity())
                .putawayById(task.getPutawayBy() != null ? task.getPutawayBy().getId() : null)
                .putawayByName(task.getPutawayBy() != null ? task.getPutawayBy().getName() : null)
                .status(task.getStatus() != null ? task.getStatus().name() : null)
                .putawayAt(task.getPutawayAt())
                .createdAt(task.getCreatedAt())
                .build();
    }
}