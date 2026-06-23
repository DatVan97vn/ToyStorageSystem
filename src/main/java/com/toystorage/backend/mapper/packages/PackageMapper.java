package com.toystorage.backend.mapper.packages;

import com.toystorage.backend.dto.response.packages.PackageResponse;
import com.toystorage.backend.models.packages.PackageBox;

public class PackageMapper {

    public static PackageResponse toResponse(PackageBox box) {

        if (box == null) {
            return null;
        }

        return PackageResponse.builder()
                .id(box.getId())
                .packageCode(box.getPackageCode())

                .transferId(
                        box.getTransfer() != null
                                ? box.getTransfer().getId()
                                : null
                )

                .transferCode(
                        box.getTransfer() != null
                                ? box.getTransfer().getTransferCode()
                                : null
                )

                .fromWarehouseId(
                        box.getTransfer() != null &&
                                box.getTransfer().getFromWarehouse() != null
                                ? box.getTransfer()
                                .getFromWarehouse()
                                .getId()
                                : null
                )

                .fromWarehouseName(
                        box.getTransfer() != null &&
                                box.getTransfer().getFromWarehouse() != null
                                ? box.getTransfer()
                                .getFromWarehouse()
                                .getName()
                                : null
                )

                .toWarehouseId(
                        box.getTransfer() != null &&
                                box.getTransfer().getToWarehouse() != null
                                ? box.getTransfer()
                                .getToWarehouse()
                                .getId()
                                : null
                )

                .toWarehouseName(
                        box.getTransfer() != null &&
                                box.getTransfer().getToWarehouse() != null
                                ? box.getTransfer()
                                .getToWarehouse()
                                .getName()
                                : null
                )

                .warehouseId(
                        box.getWarehouse() != null
                                ? box.getWarehouse().getId()
                                : null
                )

                .warehouseName(
                        box.getWarehouse() != null
                                ? box.getWarehouse().getName()
                                : null
                )

                .status(
                        box.getStatus() != null
                                ? box.getStatus().name()
                                : null
                )

                .packedAt(box.getPackedAt())
                .sealedAt(box.getSealedAt())
                .note(box.getNote())
                .build();
    }
}