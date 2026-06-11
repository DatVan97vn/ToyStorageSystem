package com.toystorage.backend.repository.manifests;

import com.toystorage.backend.models.manifests.ShipmentManifest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentManifestRepository
        extends JpaRepository<ShipmentManifest, Long> {

    Optional<ShipmentManifest> findByManifestCode(String manifestCode);
}