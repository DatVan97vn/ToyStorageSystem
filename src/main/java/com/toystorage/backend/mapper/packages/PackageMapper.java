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