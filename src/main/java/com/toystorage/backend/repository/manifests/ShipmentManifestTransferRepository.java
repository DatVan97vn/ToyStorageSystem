package com.toystorage.backend.repository.manifests;

import com.toystorage.backend.models.manifests.ShipmentManifestTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentManifestTransferRepository
        extends JpaRepository<ShipmentManifestTransfer, Long> {

    List<ShipmentManifestTransfer> findByManifest_Id(Long manifestId);

    boolean existsByManifest_IdAndTransfer_Id(
            Long manifestId,
            Long transferId
    );

    void deleteByManifest_IdAndTransfer_Id(
            Long manifestId,
            Long transferId
    );
}