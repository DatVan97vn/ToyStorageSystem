package com.toystorage.backend.repository.manifests;

import com.toystorage.backend.models.manifests.ShipmentManifest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentManifestRepository extends JpaRepository<ShipmentManifest, Long> {

    Optional<ShipmentManifest> findByManifestCode(String manifestCode);

    List<ShipmentManifest> findByFromWarehouse_Id(Long warehouseId);

    List<ShipmentManifest> findByToWarehouse_Id(Long warehouseId);
}