package com.toystorage.backend.mapper.manifests;

import com.toystorage.backend.dto.response.manifests.ManifestTransferResponse;
import com.toystorage.backend.models.manifests.ShipmentManifestTransfer;
import com.toystorage.backend.models.transfers.StockTransfer;

public class ManifestTransferMapper {

    public static ManifestTransferResponse toResponse(
            ShipmentManifestTransfer item
    ) {
        if (item == null) {
            return null;
        }

        StockTransfer transfer = item.getTransfer();

        return ManifestTransferResponse.builder()
                .id(item.getId())
                .manifestId(
                        item.getManifest() != null
                                ? item.getManifest().getId()
                                : null
                )
                .transferId(
                        transfer != null
                                ? transfer.getId()
                                : null
                )
                .transferCode(
                        transfer != null
                                ? transfer.getTransferCode()
                                : null
                )
                .status(
                        transfer != null && transfer.getStatus() != null
                                ? transfer.getStatus().name()
                                : null
                )
                .fromWarehouseId(
                        transfer != null && transfer.getFromWarehouse() != null
                                ? transfer.getFromWarehouse().getId()
                                : null
                )
                .fromWarehouseName(
                        transfer != null && transfer.getFromWarehouse() != null
                                ? transfer.getFromWarehouse().getName()
                                : null
                )
                .toWarehouseId(
                        transfer != null && transfer.getToWarehouse() != null
                                ? transfer.getToWarehouse().getId()
                                : null
                )
                .toWarehouseName(
                        transfer != null && transfer.getToWarehouse() != null
                                ? transfer.getToWarehouse().getName()
                                : null
                )
                .build();
    }
}