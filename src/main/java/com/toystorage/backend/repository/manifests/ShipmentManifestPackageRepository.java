package com.toystorage.backend.repository.manifests;

import com.toystorage.backend.models.manifests.ShipmentManifestPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentManifestPackageRepository
        extends JpaRepository<ShipmentManifestPackage, Long> {

    List<ShipmentManifestPackage> findByManifest_Id(Long manifestId);

    boolean existsByManifest_IdAndPackageBox_Id(Long manifestId, Long packageId);

    void deleteByManifest_IdAndPackageBox_Id(Long manifestId, Long packageId);
}