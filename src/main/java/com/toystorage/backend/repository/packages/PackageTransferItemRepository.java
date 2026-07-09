package com.toystorage.backend.repository.packages;

import com.toystorage.backend.models.packages.PackageBox;
import com.toystorage.backend.models.packages.PackageTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageTransferItemRepository
        extends JpaRepository<PackageTransferItem, Long> {

    List<PackageTransferItem> findByPackageBoxId(
            Long packageId
    );

    List<PackageTransferItem> findByTransferId(
            Long transferId
    );
    List<PackageTransferItem> findByTransferItemId(
            Long transferItemId
    );

    boolean existsByPackageBoxIdAndTransferItemId(
            Long packageId,
            Long transferItemId
    );

    Optional<PackageTransferItem> findByPackageBoxIdAndTransferItemId(
            Long packageId,
            Long transferItemId
    );
}