package com.toystorage.backend.mapper.transfers;

import com.toystorage.backend.dto.response.transfers.TransferItemResponse;
import com.toystorage.backend.models.transfers.StockTransferItems;

public class TransferItemMapper {

    public static TransferItemResponse toResponse(
            StockTransferItems item
    ) {
        if (item == null) {
            return null;
        }

        return TransferItemResponse.builder()
                .id(item.getId())

                .productId(
                        item.getProduct() != null
                                ? item.getProduct().getId()
                                : null
                )

                .productName(
                        item.getProduct() != null
                                ? item.getProduct().getName()
                                : null
                )

                .requestedQuantity(item.getRequestedQuantity())
                .pickedQuantity(item.getPickedQuantity())
                .receivedQuantity(item.getReceivedQuantity())
                .build();
    }
}