package com.toystorage.backend.mapper.transfers;

import com.toystorage.backend.dto.response.transfers.TransferResponse;
import com.toystorage.backend.dto.response.warehouses.WarehouseResponse;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.transfers.StockTransferItems;

import java.util.List;

public class TransferMapper {

    public static TransferResponse toResponse(
            StockTransfer transfer
    ) {
        return toResponse(transfer, List.of());
    }

    public static TransferResponse toResponse(
            StockTransfer transfer,
            List<StockTransferItems> items
    ) {
        if (transfer == null) {
            return null;
        }

        return TransferResponse.builder()
                .id(transfer.getId())
                .transferCode(transfer.getTransferCode())

                .fromWarehouse(
                        transfer.getFromWarehouse() == null
                                ? null
                                : WarehouseResponse.builder()
                                .id(transfer.getFromWarehouse().getId())
                                .code(transfer.getFromWarehouse().getCode())
                                .name(transfer.getFromWarehouse().getName())
                                .address(transfer.getFromWarehouse().getAddress())
                                .phone(transfer.getFromWarehouse().getPhone())
                                .build()
                )

                .toWarehouse(
                        transfer.getToWarehouse() == null
                                ? null
                                : WarehouseResponse.builder()
                                .id(transfer.getToWarehouse().getId())
                                .code(transfer.getToWarehouse().getCode())
                                .name(transfer.getToWarehouse().getName())
                                .address(transfer.getToWarehouse().getAddress())
                                .phone(transfer.getToWarehouse().getPhone())
                                .build()
                )

                .status(transfer.getStatus())
                .note(transfer.getNote())
                .createdAt(transfer.getCreatedAt())
                .approvedAt(transfer.getApprovedAt())
                .completedAt(transfer.getCompletedAt())

                .items(
                        items == null
                                ? List.of()
                                : items.stream()
                                .map(TransferItemMapper::toResponse)
                                .toList()
                )

                .build();
    }
}