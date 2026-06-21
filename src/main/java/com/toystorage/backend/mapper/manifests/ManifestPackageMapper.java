package com.toystorage.backend.mapper.manifests;

import com.toystorage.backend.dto.response.manifests.ManifestPackageResponse;
import com.toystorage.backend.models.manifests.ShipmentManifestPackage;

public class ManifestPackageMapper {

    public static ManifestPackageResponse toResponse(
            ShipmentManifestPackage item
    ) {
        return ManifestPackageResponse.builder()
                .id(item.getId())

                .packageId(
                        item.getPackageBox() != null
                                ? item.getPackageBox().getId()
                                : null
                )

                .packageCode(
                        item.getPackageBox() != null
                                ? item.getPackageBox().getPackageCode()
                                : null
                )

                .status(
                        item.getPackageBox() != null
                                && item.getPackageBox().getStatus() != null
                                ? item.getPackageBox().getStatus().name()
                                : null
                )

                .build();
    }
}