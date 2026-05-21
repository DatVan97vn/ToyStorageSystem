package com.toystorage.backend.repository.manifests;

import com.toystorage.backend.models.manifests.ShipmentManifestPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentManifestPackageRepository
        extends JpaRepository<ShipmentManifestPackage, Long> {
}