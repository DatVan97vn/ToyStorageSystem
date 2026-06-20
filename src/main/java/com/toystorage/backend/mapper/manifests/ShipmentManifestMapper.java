package com.toystorage.backend.mapper.manifests;

import com.toystorage.backend.dto.response.manifests.ShipmentManifestResponse;
import com.toystorage.backend.models.manifests.ShipmentManifest;

public class ShipmentManifestMapper {

    public static ShipmentManifestResponse toResponse(ShipmentManifest manifest) {

        return ShipmentManifestResponse.builder()
                .id(manifest.getId())
                .manifestCode(manifest.getManifestCode())

                .fromWarehouseId(
                        manifest.getFromWarehouse() != null
                                ? manifest.getFromWarehouse().getId()
                                : null
                )
                .fromWarehouseName(
                        manifest.getFromWarehouse() != null
                                ? manifest.getFromWarehouse().getName()
                                : null
                )

                .toWarehouseId(
                        manifest.getToWarehouse() != null
                                ? manifest.getToWarehouse().getId()
                                : null
                )
                .toWarehouseName(
                        manifest.getToWarehouse() != null
                                ? manifest.getToWarehouse().getName()
                                : null
                )

                .status(manifest.getStatus())

                .createdById(
                        manifest.getCreatedBy() != null
                                ? manifest.getCreatedBy().getId()
                                : null
                )
                .createdByUsername(
                        manifest.getCreatedBy() != null
                                ? manifest.getCreatedBy().getName()
                                : null
                )

                .createdAt(manifest.getCreatedAt())
                .build();
    }
}